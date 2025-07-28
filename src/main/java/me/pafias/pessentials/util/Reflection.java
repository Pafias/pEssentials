package me.pafias.pessentials.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.mojang.authlib.GameProfile;
import me.pafias.pessentials.pEssentials;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public static void setGameProfile(final Player player, final GameProfile profile) {
        try {
            final Object el = player.getClass().getMethod("getHandle").invoke(player);
            final Class eh = el.getClass().getSuperclass();
            final Field[] fields = eh.getDeclaredFields();
            Field field = null;
            for (Field f : fields)
                if (f.getType().getSimpleName().equals("GameProfile")) {
                    field = f;
                    break;
                }
            if (field == null)
                throw new NullPointerException("GameProfile field not found.");
            field.setAccessible(true);
            field.set(el, profile);
            field.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static GameProfile getGameProfile(final Player player) {
        try {
            return (GameProfile) player.getClass().getDeclaredMethod("getProfile").invoke(player);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendActionbar(final Player player, final String text) {
        if (plugin.parseVersion() > 9)
            try {
                player.sendActionBar(text);
            } catch (Throwable t) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
            }
        else {
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
    }

    public static org.bukkit.inventory.ItemStack getSkull() {
        if (plugin.parseVersion() < 13) {
            try {
                final Class clazz = Class.forName("org.bukkit.inventory.ItemStack");
                final Constructor constructor = clazz.getConstructor(int.class, int.class, short.class);
                final Object itemstack = constructor.newInstance(397, 1, (short) SkullType.PLAYER.ordinal());
                return (ItemStack) itemstack;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return new ItemStack(Material.getMaterial("PLAYER_HEAD"));
        }
    }

    public static void playSound(final Player player, final Sound sound, final double x, final double y, final double z, final float volume, final float pitch) {
        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.NAMED_SOUND_EFFECT);
        packet.getModifier().write(0, sound.ordinal());
        packet.getModifier().write(1, 0);
        packet.getIntegers().write(2, (int) x);
        packet.getIntegers().write(3, (int) y);
        packet.getIntegers().write(4, (int) z);
        packet.getFloat().write(0, volume);
        packet.getFloat().write(1, pitch);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    private static String getVersion() {
        return plugin.getServer().getClass().getPackage().getName().replace("org.bukkit.craftbukkit", "").replace(".", "");
    }

}
