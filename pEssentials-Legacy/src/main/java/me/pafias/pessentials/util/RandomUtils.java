package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RandomUtils {

    private static final pEssentials plugin = pEssentials.get();

    public static Collection<Player> getStaffOnline(String perm) {
        final Collection<Player> staffOnline = new ArrayList<>();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.hasPermission(perm))
                staffOnline.add(p);
        }
        return staffOnline;
    }

    public static boolean containsIgnoreCase(Collection<String> collection, String s) {
        for (String ss : collection)
            if (s.equalsIgnoreCase(ss)) return true;
        return false;
    }

    public static List<String> tabCompletePlayers(CommandSender sender, String input) {
        final String prefix = input.toLowerCase();
        final Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        final List<String> result = new ArrayList<>(plugin.getServer().getOnlinePlayers().size());
        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            if (senderPlayer == null || senderPlayer.canSee(player)) {
                if (player.getName().regionMatches(true, 0, prefix, 0, prefix.length())) {
                    result.add(player.getName());
                }
            }
        }
        return result;
    }

}
