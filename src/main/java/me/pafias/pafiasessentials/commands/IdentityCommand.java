package me.pafias.pafiasessentials.commands;

import com.mojang.authlib.GameProfile;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import me.pafias.pafiasessentials.util.GameProfileBuilder;
import me.pafias.pafiasessentials.util.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class IdentityCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public IdentityCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.identity")) {
            if (args.length == 0) {
                sender.sendMessage(CC.t("&c/" + label + " <reset/<name>> [skin] [-f]"));
                sender.sendMessage(CC.t("&6[skin] = the player who's skin you want to have"));
                sender.sendMessage(CC.t("&6[-f] = putting -f at the end will force a skin fetch from mojang (usually not needed)"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return true;
            }
            Player player = (Player) sender;
            User user = plugin.getSM().getUserManager().getUser(player.getUniqueId());
            if (args[0].equalsIgnoreCase("reset")) {
                if (!user.hasIdentity()) {
                    sender.sendMessage(CC.t("&cYou are already yourself"));
                    return true;
                }
                user.setIdentity(user.getOriginalGameProfile());
                sender.sendMessage(CC.t("&aIdentity restored."));
            } else {
                boolean hasSkin = args.length == 2;
                String name = args[0];
                if (name.length() > 16) {
                    sender.sendMessage(CC.t("&cName cannot be longer than 16 characters."));
                    return true;
                }
                String skin = name;
                if (hasSkin)
                    skin = args[1];
                if (skin.length() > 16) {
                    sender.sendMessage(CC.t("&cName of skin player cannot be longer than 16 characters."));
                    return true;
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
        return true;
    }

}
