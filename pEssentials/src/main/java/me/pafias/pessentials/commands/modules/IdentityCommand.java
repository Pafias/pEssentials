package me.pafias.pessentials.commands.modules;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.CC;
import me.pafias.putils.Tasks;
import me.pafias.putils.builders.PlayerProfileBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class IdentityCommand extends ICommand {

    public IdentityCommand() {
        super("identity", "essentials.identity", "Change your identity (disguise)", "/id <reset/<player>> [skin]", "id");
        disallowKnownPlayers = getPlugin().getConfig().getBoolean("identity.disallow_known_players");
        nameBlacklist = new HashSet<>(getPlugin().getConfig().getStringList("identity.name_blacklist"));
    }

    private final boolean disallowKnownPlayers;
    private final Set<String> nameBlacklist;

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.t("&c/" + label + " <reset/<name>> [skin]"));
            sender.sendMessage(CC.t("&6[skin] = the player who's skin you want to have"));
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final User user = plugin.getSM().getUserManager().getUser(player.getUniqueId());
        if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase(user.getOriginalGameProfile().getName())) {
            if (!user.hasIdentity()) {
                sender.sendMessage(CC.t("&cYou are already yourself"));
                return;
            }
            user.setIdentity(user.getOriginalGameProfile());
            sender.sendMessage(CC.t("&aIdentity restored."));
        } else {
            boolean hasSkin = args.length == 2;
            final String name = args[0];
            if (name.length() > 16) {
                sender.sendMessage(CC.t("&cName cannot be longer than 16 characters."));
                return;
            }
            if (RandomUtils.containsIgnoreCase(nameBlacklist, name)) {
                sender.sendMessage(CC.t("&cYou cannot use that name."));
                return;
            }
            sender.sendMessage(CC.t("&aProcessing..."));
            Tasks.runAsync(() -> {
                if (disallowKnownPlayers) {
                    final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if ((offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) && !offlinePlayer.equals(player)) {
                        sender.sendMessage(CC.t("&cYou cannot use this name."));
                        return;
                    }
                }
                String skin = name;
                if (hasSkin)
                    skin = args[1];
                if (skin.length() > 16) {
                    sender.sendMessage(CC.t("&cName of skin player cannot be longer than 16 characters."));
                    return;
                }
                try {
                    PlayerProfile skinProfile;
                    if (skin.equals(user.getOriginalGameProfile().getName()))
                        skinProfile = user.getOriginalGameProfile();
                    else
                        skinProfile = new PlayerProfileBuilder()
                                .setName(skin)
                                .setGenerateUuidFromName(false)
                                .setFetchProperties(true)
                                .build();

                    final PlayerProfile profile = new PlayerProfileBuilder()
                            .setProperties(skinProfile.getProperties())
                            .setUuid(user.getUUID())
                            .setName(name)
                            .build();

                    Tasks.runSync(() -> {
                        user.setIdentity(profile);
                        sender.sendMessage(CC.t("&aIdentity changed."));
                    });
                } catch (Exception ex) {
                    sender.sendMessage(CC.t("&cSomething went wrong. &oDoes that player exist?"));
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
                    .toList()
            );
            return list;
        } else if (args.length == 2)
            return RandomUtils.tabCompletePlayers(sender, args[1]);
        else return Collections.emptyList();
    }

}
