package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomUtils {

    private static final pEssentials plugin = pEssentials.get();

    public static Set<Player> getStaffOnline(String perm) {
        final Set<Player> staffOnline = new HashSet<>();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.hasPermission(perm))
                staffOnline.add(p);
        }
        return staffOnline;
    }

    public static boolean containsIgnoreCase(Set<String> nameBlacklist, String name) {
        for (String s : nameBlacklist)
            if (s.equalsIgnoreCase(name)) return true;
        return false;
    }

    public static List<String> tabCompletePlayers(CommandSender sender, String input) {
        final String prefix = input.toLowerCase();
        final Player senderPlayer = sender instanceof Player p ? p : null;
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
