package me.pafias.pafiasessentials.nms;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import me.pafias.pafiasessentials.commands.RickrollCommand;
import me.pafias.pafiasessentials.events.PlayerRickrollEndedEvent;
import me.pafias.pafiasessentials.events.PlayerRickrolledEvent;
import me.pafias.pafiasessentials.util.CC;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class VersionProvider1_19_R1 implements NMSProvider {

    @Override
    public int getPing(Player player) {
        return player.getPing();
    }

    @Override
    public void setGameProfile(Player player, GameProfile profile) {
        try {
            CraftPlayer cp = ((CraftPlayer) player);
            EntityLiving el = cp.getHandle();
            Field gp2 = el.getClass().getSuperclass().getDeclaredField("ct");
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
        return entity.isInvisible();
    }

    @Override
    public void toggleInvisibility(LivingEntity entity) {
        entity.setInvisible(!entity.isInvisible());
    }

    @Override
    public ItemStack getSkull() {
        return new ItemStack(Material.PLAYER_HEAD, 1);
    }

    @Override
    public void crash(Player player) {
        player.spawnParticle(Particle.CRIT, player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ(), Integer.MAX_VALUE);
    }

    @Override
    public void sendCustomPayload(Player player, String channel, byte[] bytes) {
        PacketDataSerializer pds = new PacketDataSerializer(Unpooled.wrappedBuffer(bytes));
        PacketPlayOutCustomPayload payloadPacket = new PacketPlayOutCustomPayload(new MinecraftKey(channel), pds);
        ((CraftPlayer) player).getHandle().b.a(payloadPacket);
    }

    @Override
    public void sendParticle(Player player, String particle, double x, double y, double z, int amount) {
        player.spawnParticle(Particle.valueOf(particle.toUpperCase().trim()), x, y, z, amount);
    }

    @Override
    public void playSound(Player player, Sound sound, double x, double y, double z, float volume, float pitch) {
        player.playSound(player, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void rickroll(Player player, @Nullable CommandSender sender) {
        try {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(new MinecraftKey("music.rickroll"), net.minecraft.sounds.SoundCategory.a, new Vec3D(player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ()), Float.MAX_VALUE, 1f, 0);
            connection.a(packet);
            if (sender != null) sender.sendMessage(CC.tf("&e%s just got rickrolled!", player.getName()));
            EntityPlayer npc = new EntityPlayer(((CraftPlayer) player).getHandle().c, ((CraftPlayer) player).getHandle().x(), RickrollCommand.rickastley, null);
            Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(2.5));
            Location npcloc = loc.setDirection(player.getLocation().subtract(loc).toVector());
            float yaw = npcloc.getYaw();
            float pitch = npcloc.getPitch();
            npc.a(npcloc.getX(), npcloc.getY(), npcloc.getZ(), npcloc.getYaw(), npcloc.getPitch());
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc));
            connection.a(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.aH, (byte) ((yaw % 360.) * 256 / 360), (byte) ((pitch % 360.) * 256 / 360), false));
            connection.a(new PacketPlayOutEntityHeadRotation(npc, (byte) ((yaw % 360.) * 256 / 360)));
            RickrollCommand.entities.put(player.getUniqueId(), npc);
            plugin.getServer().getPluginManager().callEvent(new PlayerRickrolledEvent(sender, player, npc));
            new BukkitRunnable() {
                @Override
                public void run() {
                    RickrollCommand.requested.remove(player.getUniqueId());
                    RickrollCommand.entities.remove(player.getUniqueId());
                    try {
                        player.stopSound("music.rickroll");
                        ((CraftPlayer) player).getHandle().b.a(new PacketPlayOutEntityDestroy(npc.aH));
                        ((CraftPlayer) player).getHandle().b.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, npc));
                        npc.b.disconnect("");
                        plugin.getServer().getPluginManager().callEvent(new PlayerRickrollEndedEvent(sender, player));
                    } catch (Exception ignored) {
                    }
                }
            }.runTaskLater(plugin, 17 * 20);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleRickrollMove(PlayerMoveEvent event) {
        EntityPlayer npc = (EntityPlayer) RickrollCommand.entities.get(event.getPlayer().getUniqueId());
        if (npc != null) {
            Location loc = event.getPlayer().getLocation().add(event.getPlayer().getLocation().getDirection().multiply(2.5));
            Location npcloc = loc.setDirection(event.getPlayer().getLocation().subtract(loc).toVector());
            float yaw = npcloc.getYaw();
            float pitch = npcloc.getPitch();
            PlayerConnection connection = ((CraftPlayer) event.getPlayer()).getHandle().b;
            connection.a(new PacketPlayOutEntityDestroy(npc.aH));
            npc.a(npcloc.getX(), npcloc.getY(), npcloc.getZ(), npcloc.getYaw(), npcloc.getPitch());
            connection.a(new PacketPlayOutNamedEntitySpawn(npc));
            connection.a(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.aH, (byte) ((yaw % 360.) * 256 / 360), (byte) ((pitch % 360.) * 256 / 360), false));
            connection.a(new PacketPlayOutEntityHeadRotation(npc, (byte) ((yaw % 360.) * 256 / 360)));
        }
    }

    @Override
    public void handleRickrollQuit(PlayerQuitEvent event) {
        if (RickrollCommand.entities.containsKey(event.getPlayer().getUniqueId())) {
            EntityPlayer npc = (EntityPlayer) RickrollCommand.entities.get(event.getPlayer().getUniqueId());
            RickrollCommand.requested.remove(event.getPlayer().getUniqueId());
            RickrollCommand.entities.remove(event.getPlayer().getUniqueId());
            try {
                ((CraftPlayer) event.getPlayer()).getHandle().b.a(new PacketPlayOutEntityDestroy(npc.aH));
                ((CraftPlayer) event.getPlayer()).getHandle().b.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, npc));
                npc.b.disconnect("");
            } catch (Exception ignored) {
            }
        }
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
        return p.b() > 0;
    }

    @Override
    public boolean steeringRight(Object packet) {
        PacketPlayInSteerVehicle p = (PacketPlayInSteerVehicle) packet;
        return p.b() < 0;
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
