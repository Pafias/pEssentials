package me.pafias.pafiasessentials.nms;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class VersionProvider1_8_R3 implements NMSProvider {

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
    public void sendParticle(Player player, String particle, double x, double y, double z, int amount) {
        EnumParticle ep;
        try {
            ep = EnumParticle.a(Integer.parseInt(particle));
        } catch (NumberFormatException ex) {
            ep = EnumParticle.valueOf(particle.toUpperCase().trim());
        }
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(ep, false, (float) x, (float) y, (float) z, 0, 0, 0, 0, amount);
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
        return p.b() > 0;
    }

    @Override
    public boolean steeringBackwards(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.b() < 0;
    }

    @Override
    public boolean steeringLeft(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.a() > 0;
    }

    @Override
    public boolean steeringRight(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.a() < 0;
    }

    @Override
    public boolean steeringPressingSpace(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.c();
    }

    @Override
    public boolean steeringPressingShift(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.d();
    }

}
