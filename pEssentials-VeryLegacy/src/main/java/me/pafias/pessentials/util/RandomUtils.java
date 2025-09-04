package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class RandomUtils {

    private static final pEssentials plugin = pEssentials.get();

    public static Set<Player> getStaffOnline(String perm) {
        Set<Player> staff = new HashSet<>();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.hasPermission(perm))
                staff.add(p);
        }
        return staff;
    }

    public static boolean containsIgnoreCase(String[] array, String search) {
        for (String arg : array) {
            if (arg != null && arg.toLowerCase().contains(search.toLowerCase()))
                return true;
        }
        return false;
    }

    // Helper method for Java 7 compatibility
    public static String join(String[] array, String delimiter) {
        if (array == null || array.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

}
