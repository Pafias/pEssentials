package me.pafias.pafiasessentials.util;

import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> translate(List<String> list) {
        List<String> l = new ArrayList<>();
        list.forEach(s -> l.add(CC.translate(s)));
        return l;
    }

    public static String formatStaffchat(String name, String message) {
        return translate(PafiasEssentials.get().getSM().getVariables().staffchatFormat.replace("{player}", name).replace("{message}", message));
    }

}
