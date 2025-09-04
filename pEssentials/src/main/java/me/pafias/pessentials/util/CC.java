package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import net.kyori.adventure.text.Component;

public class CC extends me.pafias.putils.CC {

    public static Component formatStaffchatModern(String name, String message) {
        return a(pEssentials.get().getConfig().getString("staffchat_format").replace("{player}", name).replace("{message}", message));
    }

    public static String formatStaffchat(String name, String message) {
        return t(pEssentials.get().getConfig().getString("staffchat_format").replace("{player}", name).replace("{message}", message));
    }

}
