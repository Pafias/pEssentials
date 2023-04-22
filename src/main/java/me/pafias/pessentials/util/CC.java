package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class CC {

    public static String t(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String tf(String s, Object... o) {
        return t(String.format(s, o));
    }

    public static List<String> t(List<String> list) {
        return list.stream().map(CC::t).collect(Collectors.toList());
    }

    public static String formatStaffchat(String name, String message) {
        return t(pEssentials.get().getSM().getVariables().staffchatFormat.replace("{player}", name).replace("{message}", message));
    }

}
