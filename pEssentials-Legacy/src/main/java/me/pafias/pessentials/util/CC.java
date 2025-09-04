package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class CC {

    public static TextComponent a(String s) {
        if (s == null) return null;
        return LegacyComponentSerializer.legacyAmpersand().deserialize(s);
    }

    public static TextComponent af(String s, Object... o) {
        if (s == null) return null;
        return a(String.format(s, o));
    }

    public static List<Component> a(List<String> list) {
        if (list == null) return null;
        return list.stream().map(CC::a).collect(Collectors.toList());
    }

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
        return list.stream().map(CC::t).collect(Collectors.toList());
    }

    public static TextComponent formatStaffchatModern(String name, String message) {
        return a(pEssentials.get().getConfig().getString("staffchat_format").replace("{player}", name).replace("{message}", message));
    }

    public static String formatStaffchat(String name, String message) {
        return t(pEssentials.get().getConfig().getString("staffchat_format").replace("{player}", name).replace("{message}", message));
    }

}
