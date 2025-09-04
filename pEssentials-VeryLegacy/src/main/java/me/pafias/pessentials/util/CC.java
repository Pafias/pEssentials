package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {

    public static String t(String s) {
        if (s == null) return null;
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String tf(String s, Object... o) {
        if (s == null) return null;
        return t(String.format(s, o));
    }

    public static List<String> t(List<String> list) {
        if (list == null) return null;
        List<String> newList = new ArrayList<>();
        for (String s : list)
            newList.add(t(s));
        return newList;
    }

    public static String formatStaffchat(String name, String message) {
        return t(pEssentials.get().getConfig().getString("staffchat_format").replace("{player}", name).replace("{message}", message));
    }

}
