package me.pafias.pafiasessentials.commands.modules;

import com.mojang.authlib.GameProfile;
import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import me.pafias.pafiasessentials.util.GameProfileBuilder;
import me.pafias.pafiasessentials.util.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IdentityCommand extends ICommand {

    public IdentityCommand() {
        super("identity", "essentials.identity", "Change your identity (disguise)", "/id <reset/<player>> [skin] [-f]", "id");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.identity")) {
            if (args.length == 0) {
                sender.sendMessage(CC.t("&c/" + label + " <reset/<name>> [skin] [-f]"));
                sender.sendMessage(CC.t("&6[skin] = the player who's skin you want to have"));
                sender.sendMessage(CC.t("&6[-f] = putting -f at the end will force a skin fetch from mojang (usually not needed)"));
                return;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            Player player = (Player) sender;
            User user = plugin.getSM().getUserManager().getUser(player.getUniqueId());
            if (args[0].equalsIgnoreCase("reset")) {
                if (!user.hasIdentity()) {
                    sender.sendMessage(CC.t("&cYou are already yourself"));
                    return;
                }
                user.setIdentity(user.getOriginalGameProfile());
                sender.sendMessage(CC.t("&aIdentity restored."));
            } else {
                boolean hasSkin = args.length == 2;
                String name = args[0];
                if (name.length() > 16) {
                    sender.sendMessage(CC.t("&cName cannot be longer than 16 characters."));
                    return;
                }
                String skin = name;
                if (hasSkin)
                    skin = args[1];
                if (skin.length() > 16) {
                    sender.sendMessage(CC.t("&cName of skin player cannot be longer than 16 characters."));
                    return;
                }
                String finalSkin = skin;
                sender.sendMessage(CC.t("&aProcessing..."));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            GameProfile profile = new GameProfile(user.getUUID(), name);
                            GameProfile skinGP = finalSkin.equals(user.getOriginalGameProfile().getName()) ? user.getOriginalGameProfile() : GameProfileBuilder.fetch(UUIDFetcher.getUUID(finalSkin), args.length >= 3 && args[2].equalsIgnoreCase("-f"));
                            skinGP.getProperties().keySet().forEach(key -> skinGP.getProperties().get(key).forEach(property -> profile.getProperties().put(key, property)));
                            user.setIdentity(profile);
                            sender.sendMessage(CC.t("&aIdentity changed."));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage(CC.t("&cSomething went wrong. &oDoes that player exist?"));
                        }
                    }
                }.runTaskAsynchronously(plugin);
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("reset");
            list.addAll(plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
            return list;
        } else if (args.length == 2)
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        else if (args.length == 3)
            return Collections.singletonList("-f");
        else return Collections.emptyList();
    }

}
