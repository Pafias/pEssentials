package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SoundCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public SoundCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.sound")) {
            if (args.length == 0) {
                sender.sendMessage(CC.t("&2/" + label + " play <player/all> <sound> [omnipresent] [volume] [pitch] &f- Plays a sound"));
                sender.sendMessage(CC.t("&2/" + label + " stop <player/all> &f- Stops a sound"));
                return true;
            }
            if (args[0].equalsIgnoreCase("play") && sender.hasPermission("essentials.sound.play")) {
                Set<Player> target;
                Sound sound;
                if (args.length < 3) {
                    sender.sendMessage(CC.t("&c/" + label + " " + args[0] + " play <player/all> <sound> [omnipresent] [volume] [pitch]"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                    target = new HashSet<>(plugin.getServer().getOnlinePlayers());
                } else {
                    target = new HashSet<>(Collections.singleton(plugin.getServer().getPlayer(args[1])));
                }
                if (target == null || target.isEmpty()) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return true;
                }
                try {
                    sound = Sound.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException ex) {
                    sender.sendMessage(CC.t("&cInvalid sound!"));
                    return true;
                }
                boolean omnipresent = false;
                if (args.length > 3)
                    try {
                        omnipresent = Boolean.parseBoolean(args[3]);
                    } catch (IllegalArgumentException ex) {
                        sender.sendMessage(CC.t("&cInvalid omnipresent value! Use 'true' or 'false'"));
                        return true;
                    }
                float volume = omnipresent ? Float.MAX_VALUE : 1.0F;
                if (args.length > 4)
                    try {
                        volume = Float.parseFloat(args[4]);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(CC.t("&cInvalid volume level!"));
                        return true;
                    }
                float pitch = 1.0F;
                if (args.length > 5)
                    try {
                        pitch = Float.parseFloat(args[5]);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(CC.t("&cInvalid pitch level!"));
                        return true;
                    }
                for (Player t : target) {
                    if (omnipresent) {
                        plugin.getSM().getNMSProvider().playSound(t, sound, t.getEyeLocation().getX(), t.getEyeLocation().getY(), t.getEyeLocation().getZ(), volume, pitch);
                        continue;
                    }
                    t.playSound(t.getEyeLocation(), sound, volume, pitch);
                }
                sender.sendMessage(CC.t("&aSound played!"));
            } else if (args[0].equalsIgnoreCase("stop") && sender.hasPermission("essentials.sound.stop")) {
                Set<Player> target;
                if (args.length < 2) {
                    sender.sendMessage(CC.t("&c/" + label + " " + args[0] + " stop <player/all>"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                    target = new HashSet<>(plugin.getServer().getOnlinePlayers());
                } else {
                    target = new HashSet<>(Collections.singleton(plugin.getServer().getPlayer(args[1])));
                }
                if (target == null || target.isEmpty()) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return true;
                }
                for (Player t : target) {
                    for (Sound sound : Sound.values())
                        t.stopSound(sound);
                }
                sender.sendMessage(CC.t("&aSound(s) stopped!"));
            }
        }
        return true;
    }

}
