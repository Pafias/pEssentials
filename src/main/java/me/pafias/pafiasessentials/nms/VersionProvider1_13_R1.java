package me.pafias.pafiasessentials.nms;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Particle;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class VersionProvider1_13_R1 implements NMSProvider {

    @Override
    public int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    @Override
    public void setGameProfile(Player player, GameProfile profile) {
        try {
            CraftPlayer cp = ((CraftPlayer) player);
            EntityLiving el = cp.getHandle();
            Field gp2 = el.getClass().getSuperclass().getDeclaredField("g");
            gp2.setAccessible(true);
            gp2.set(el, profile);
            gp2.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public GameProfile getGameProfile(Player player) {
        return ((CraftPlayer) player).getProfile();
    }

    @Override
    public void sendActionbar(Player player, String text) {
        player.sendActionBar(text);
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
        is.setDamage(SkullType.PLAYER.ordinal());
        return CraftItemStack.asBukkitCopy(is);
    }

    @Override
    public void crash(Player player) {
        player.spawnParticle(Particle.CRIT, player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ(), Integer.MAX_VALUE);
    }

    @Override
    public void sendParticle(Player player, String particle, double x, double y, double z, int amount) {
        net.minecraft.server.v1_13_R2.Particle<? extends ParticleParam> ep;
        try {

            ep = IRegistry.PARTICLE_TYPE.fromId(Integer.parseInt(particle));
        } catch (NumberFormatException ex) {
            ep = IRegistry.PARTICLE_TYPE.get(new MinecraftKey(particle.toUpperCase().trim()));
        }
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles((ParticleParam) ep, false, (float) x, (float) y, (float) z, 0, 0, 0, 0, amount);
        CraftPlayer cp = (CraftPlayer) player;
        EntityPlayer cep = cp.getHandle();
        cep.playerConnection.sendPacket(packet);
    }

    @Override
    public void playSound(Player player, Sound sound, double x, double y, double z, float volume, float pitch) {
        PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(new MinecraftKey(sound.toString()), SoundCategory.MASTER, new Vec3D(x, y, z), volume, pitch);
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
        return p.c() > 0;
    }

    @Override
    public boolean steeringRight(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.c() < 0;
    }

    @Override
    public boolean steeringPressingSpace(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.d();
    }

    @Override
    public boolean steeringPressingShift(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.e();
    }

}
