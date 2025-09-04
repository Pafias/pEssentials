package me.pafias.pessentials.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ChatUtil {

    public static void sendClickableMessage(Player player, String message, String url) {
        try {
            // The JSON chat structure (same as 1.6.4 client expects)
            String json = "{text:\"" + message + "\",clickEvent:{action:open_url,value:\"" + url + "\"}}";

            // Get classes
            Class<?> chatSerializer = getNMSClass("net.minecraft.server." + getVersion() + ".ChatSerializer");
            Class<?> iChatBaseComponent = getNMSClass("net.minecraft.server." + getVersion() + ".IChatBaseComponent");
            Class<?> packetPlayOutChat = getNMSClass("net.minecraft.server." + getVersion() + ".PacketPlayOutChat");

            // Call ChatSerializer.a(json) to get IChatBaseComponent
            Method aMethod = chatSerializer.getMethod("a", String.class);
            Object baseComponent = aMethod.invoke(null, json);

            // Construct the chat packet
            Constructor<?> packetConstructor = packetPlayOutChat.getConstructor(iChatBaseComponent);
            Object packet = packetConstructor.newInstance(baseComponent);

            // Send packet to player
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            Method sendPacket = playerConnection.getClass().getMethod("sendPacket", getNMSClass("net.minecraft.server." + getVersion() + ".Packet"));
            sendPacket.invoke(playerConnection, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getVersion() {
        return org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    private static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }
}
