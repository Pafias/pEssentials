package me.pafias.pessentials.commands.modules;

import com.mojang.authlib.GameProfile;
import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.GameProfileBuilder;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.pessentials.util.UUIDFetcher;
import me.pafias.putils.LCC;
import me.pafias.putils.Tasks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class IdentityCommand extends ICommand {

    public IdentityCommand() {
        super("identity", "essentials.identity", "Change your identity (disguise)", "/id <reset/<player>> [skin] [-f]", "id");
        nameBlacklist = new HashSet<>(getPlugin().getConfig().getStringList("identity.name_blacklist"));
    }

    private final Set<String> nameBlacklist;

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(LCC.t("&c/" + label + " <reset/<name>> [skin] [-f]"));
            sender.sendMessage(LCC.t("&6[skin] = the player who's skin you want to have"));
            sender.sendMessage(LCC.t("&6[-f] = putting -f at the end will force a skin fetch from mojang (usually not needed)"));
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(LCC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        final User user = plugin.getSM().getUserManager().getUser(player.getUniqueId());
        if (args[0].equalsIgnoreCase("reset")) {
            if (!user.hasIdentity()) {
                sender.sendMessage(LCC.t("&cYou are already yourself"));
                return;
            }
            user.setIdentity(user.getOriginalGameProfile());
            sender.sendMessage(LCC.t("&aIdentity restored."));
        } else {
            boolean hasSkin = args.length == 2;
            final String name = args[0];
            if (name.length() > 16) {
                sender.sendMessage(LCC.t("&cName cannot be longer than 16 characters."));
                return;
            }
            if (RandomUtils.containsIgnoreCase(nameBlacklist, name)) {
                sender.sendMessage(LCC.t("&cYou cannot use that name."));
                return;
            }
            String skin = name;
            if (hasSkin)
                skin = args[1];
            if (skin.length() > 16) {
                sender.sendMessage(LCC.t("&cName of skin player cannot be longer than 16 characters."));
                return;
            }
            final String finalSkin = skin;
            sender.sendMessage(LCC.t("&aProcessing..."));
            Tasks.runAsync(() -> {
                try {
                    final GameProfile profile = new GameProfile(user.getUUID(), name);
                    final GameProfile skinGP = finalSkin.equals(user.getOriginalGameProfile().getName()) ? user.getOriginalGameProfile() : GameProfileBuilder.fetch(UUIDFetcher.getUUID(finalSkin), args.length >= 3 && args[2].equalsIgnoreCase("-f"));
                    skinGP.getProperties().keySet().forEach(key -> skinGP.getProperties().get(key).forEach(property -> profile.getProperties().put(key, property)));
                    user.setIdentity(profile);
                    sender.sendMessage(LCC.t("&aIdentity changed."));
                } catch (Exception ex) {
                    sender.sendMessage(LCC.t("&cSomething went wrong. &oDoes that player exist?"));
                }
            });
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("reset");
            list.addAll(plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList())
            );
            return list;
        } else if (args.length == 2)
            return RandomUtils.tabCompletePlayers(sender, args[1]);
        else if (args.length == 3)
            return Collections.singletonList("-f");
        else return Collections.emptyList();
    }

}
