package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SoundCommand extends ICommand {

    public SoundCommand() {
        super("sound", "essentials.sound", "Sound utils", "/sound play <player>/all [sound] [volume] [pitch]", "snd");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.t("&2/" + label + " play <player/all> <sound> [volume] [pitch] &f- Plays a sound"));
            return;
        }
        if (args[0].equalsIgnoreCase("play") && sender.hasPermission("essentials.sound.play")) {
            Set<Player> target;
            Sound sound;
            if (args.length < 3) {
                sender.sendMessage(CC.t("&c/" + label + " " + args[0] + " play <player/all> <sound> [volume] [pitch]"));
                return;
            }
            if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                target = new HashSet<>(Arrays.asList(plugin.getServer().getOnlinePlayers()));
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
            float volume = 1.0F;
            if (args.length > 3)
                try {
                    volume = Float.parseFloat(args[3]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid volume level!"));
                    return;
                }
            float pitch = 1.0F;
            if (args.length > 4)
                try {
                    pitch = Float.parseFloat(args[4]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid pitch level!"));
                    return;
                }
            for (Player t : target) {
                t.playSound(t.getEyeLocation(), sound, volume, pitch);
            }
            sender.sendMessage(CC.t("&aSound played!"));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("play");
        } else if (args.length == 2) {
            final List<String> list = new ArrayList<>();
            list.add("all");
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (sender instanceof Player && !((Player) sender).canSee(p)) continue;
                String name = p.getName();
                if (name != null && name.toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(name);
                }
            }
            return list;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("play")) {
            List<String> sounds = new ArrayList<>();
            for (Sound s : Sound.values()) {
                String name = s.name();
                if (name.toLowerCase().startsWith(args[2].toLowerCase())) {
                    sounds.add(name);
                }
            }
            return sounds;
        } else {
            return Collections.emptyList();
        }
    }

}
