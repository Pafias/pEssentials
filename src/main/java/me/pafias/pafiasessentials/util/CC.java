package me.pafias.pafiasessentials.util;

import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {

    public static String t(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String tf(String s, Object... o) {
        return t(String.format(s, o));
    }

    public static List<String> t(List<String> list) {
        List<String> l = new ArrayList<>();
        list.forEach(s -> l.add(CC.t(s)));
        return l;
    }

    public static String formatStaffchat(String name, String message) {
        return t(PafiasEssentials.get().getSM().getVariables().staffchatFormat.replace("{player}", name).replace("{message}", message));
    }

}
