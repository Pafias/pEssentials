package me.pafias.pafiasessentials.nms;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class VersionProvider1_7_R1 implements NMSProvider {

    @Override
    public int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    @Override
    public void setGameProfile(Player player, GameProfile profile) {
        try {
            CraftPlayer cp = ((CraftPlayer) player);
            EntityLiving el = cp.getHandle();
            Field gp2 = el.getClass().getSuperclass().getDeclaredField("bH");
            gp2.setAccessible(true);
            gp2.set(el, profile);
            gp2.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public GameProfile getGameProfile(Player player) {
        net.minecraft.util.com.mojang.authlib.GameProfile p = ((CraftPlayer) player).getHandle().getProfile();
        return new GameProfile(p.getId(), p.getName());
    }

    @Override
    public void sendActionbar(Player player, String text) {
        IChatBaseComponent chatBaseComponent = ChatSerializer.a(ChatSerializer.a(new ChatComponentText(text)));
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

    @Override
    public boolean isInvisible(LivingEntity entity) {
        if (!(entity instanceof ArmorStand))
            return false;
        ArmorStand as = (ArmorStand) entity;
        return !as.isVisible();
    }

    @Override
    public void toggleInvisibility(LivingEntity entity) {
        if (!(entity instanceof ArmorStand))
            return;
        ArmorStand as = (ArmorStand) entity;
        as.setVisible(!as.isVisible());
    }

    @Override
    public org.bukkit.inventory.ItemStack getSkull() {
        ItemStack is = new ItemStack(Item.getById(397));
        is.setData(SkullType.PLAYER.ordinal());
        return CraftItemStack.asBukkitCopy(is);
    }

    @Override
    public void crash(Player player) {
        sendParticle(player, "CRIT", player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ(), Integer.MAX_VALUE);
    }

    @Override
    public void sendParticle(Player player, String particle, double x, double y, double z, int amount) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle.toUpperCase().trim(), (float) x, (float) y, (float) z, 0, 0, 0, 0, amount);
        CraftPlayer cp = (CraftPlayer) player;
        EntityPlayer cep = cp.getHandle();
        cep.playerConnection.sendPacket(packet);
    }

    @Override
    public void playSound(Player player, Sound sound, double x, double y, double z, float volume, float pitch) {
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(sound.toString(), x, y, z, volume, pitch);
        CraftPlayer cp = (CraftPlayer) player;
        EntityPlayer cep = cp.getHandle();
        cep.playerConnection.sendPacket(packet);
    }

    @Override
    public boolean steeringForward(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.c() > 0;
    }

    @Override
    public boolean steeringBackwards(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.c() < 0;
    }

    @Override
    public boolean steeringLeft(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.d() > 0;
    }

    @Override
    public boolean steeringRight(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.d() < 0;
    }

    @Override
    public boolean steeringPressingSpace(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.e();
    }

    @Override
    public boolean steeringPressingShift(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.f();
    }

}
