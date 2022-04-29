package me.pafias.pafiasessentials.util;

import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.ChatColor;

public class CC {

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String formatStaffchat(String name, String message) {
        return translate(PafiasEssentials.get().getSM().getVariables().staffchatFormat.replace("{player}", name).replace("{message}", message));
    }

}
