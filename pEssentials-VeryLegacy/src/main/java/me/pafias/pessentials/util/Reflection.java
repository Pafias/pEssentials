package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection {

    private static final pEssentials plugin = pEssentials.get();

    public static int getPing(final Player player) {
        if (plugin.parseVersion() >= 17) {
            try {
                return (int) player.getClass().getDeclaredMethod("getPing").invoke(player);
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1;
            }
        } else {
            try {
                final Object o = player.getClass().getDeclaredMethod("getHandle").invoke(player);
                final Field f = o.getClass().getDeclaredField("ping");
                return f.getInt(o);
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1;
            }
        }
    }

    public static void sendActionbar(final Player player, final String text) {
        try {
            final String version = getVersion();
            final Class cs = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
            final Object chatComponent = cs.getDeclaredMethod("a", String.class).invoke(null, "{\"text\": \"" + text + "\"}");
            final Class cp = player.getClass();
            final Object ep = cp.getDeclaredMethod("getHandle").invoke(player);
            final Object nm = ep.getClass().getDeclaredField("playerConnection").get(ep);
            final Method sendPacket = nm.getClass().getDeclaredMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));
            final Class packetclass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
            final Constructor constructor = packetclass.getDeclaredConstructor(Class.forName("net.minecraft.server." + version + ".IChatBaseComponent"), byte.class);
            final Object packet = constructor.newInstance(chatComponent, (byte) 2);
            sendPacket.invoke(nm, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getVersion() {
        return plugin.getServer().getClass().getPackage().getName().replace("org.bukkit.craftbukkit", "").replace(".", "");
    }

}
