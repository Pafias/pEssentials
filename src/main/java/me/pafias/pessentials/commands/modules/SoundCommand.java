package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Reflection;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class SoundCommand extends ICommand {

    public SoundCommand() {
        super("sound", "essentials.sound", "Sound utils", "/sound play/stop <player>/all [sound] [omnipresent] [volume] [pitch]", "snd");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.t("&2/" + label + " play <player/all> <sound> [omnipresent] [volume] [pitch] &f- Plays a sound"));
            sender.sendMessage(CC.t("&2/" + label + " stop <player/all> &f- Stops a sound"));
            return;
        }
        if (args[0].equalsIgnoreCase("play") && sender.hasPermission("essentials.sound.play")) {
            Set<Player> target;
            Sound sound;
            if (args.length < 3) {
                sender.sendMessage(CC.t("&c/" + label + " " + args[0] + " play <player/all> <sound> [omnipresent] [volume] [pitch]"));
                return;
            }
            if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                target = new HashSet<>(plugin.getServer().getOnlinePlayers());
            } else {
                target = new HashSet<>(Collections.singleton(plugin.getServer().getPlayer(args[1])));
            }
            if (target == null || target.isEmpty()) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            try {
                sound = Sound.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(CC.t("&cInvalid sound!"));
                return;
            }
            boolean omnipresent = false;
            if (args.length > 3)
                try {
                    omnipresent = Boolean.parseBoolean(args[3]);
                } catch (IllegalArgumentException ex) {
                    sender.sendMessage(CC.t("&cInvalid omnipresent value! Use 'true' or 'false'"));
                    return;
                }
            float volume = omnipresent ? Float.MAX_VALUE : 1.0F;
            if (args.length > 4)
                try {
                    volume = Float.parseFloat(args[4]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid volume level!"));
                    return;
                }
            float pitch = 1.0F;
            if (args.length > 5)
                try {
                    pitch = Float.parseFloat(args[5]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid pitch level!"));
                    return;
                }
            for (Player t : target) {
                if (omnipresent) {
                    Reflection.playSound(t, sound, t.getEyeLocation().getX(), t.getEyeLocation().getY(), t.getEyeLocation().getZ(), volume, pitch);
                    continue;
                }
                t.playSound(t.getEyeLocation(), sound, volume, pitch);
            }
            sender.sendMessage(CC.t("&aSound played!"));
        } else if (args[0].equalsIgnoreCase("stop") && sender.hasPermission("essentials.sound.stop")) {
            Set<Player> target;
            if (args.length < 2) {
                sender.sendMessage(CC.t("&c/" + label + " " + args[0] + " stop <player/all>"));
                return;
            }
            if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                target = new HashSet<>(plugin.getServer().getOnlinePlayers());
            } else {
                target = new HashSet<>(Collections.singleton(plugin.getServer().getPlayer(args[1])));
            }
            if (target == null || target.isEmpty()) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            for (Player t : target) {
                for (Sound sound : Sound.values())
                    t.stopSound(sound);
            }
            sender.sendMessage(CC.t("&aSound(s) stopped!"));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return Arrays.asList("play", "stop");
        else if (args.length == 2) {
            final List<String> list = new ArrayList<>();
            list.add("all");
            list.addAll(plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(p -> p.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList()));
            return list;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("play"))
            return Arrays.stream(Sound.values())
                    .map(Sound::name)
                    .filter(s -> s.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        else if (args.length == 4 && args[0].equalsIgnoreCase("play"))
            return Arrays.asList("true", "false");
        else return Collections.emptyList();
    }

}
