package me.pafias.pafiasessentials.nms;

import com.mojang.authlib.GameProfile;
import me.pafias.pafiasessentials.commands.RickrollCommand;
import me.pafias.pafiasessentials.events.PlayerRickrollEndedEvent;
import me.pafias.pafiasessentials.events.PlayerRickrolledEvent;
import me.pafias.pafiasessentials.util.CC;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class VersionProvider1_16_R1 implements NMSProvider {

    @Override
    public int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    @Override
    public void setGameProfile(Player player, GameProfile profile) {
        try {
            CraftPlayer cp = ((CraftPlayer) player);
            EntityLiving el = cp.getHandle();
            Field gp2 = el.getClass().getSuperclass().getDeclaredField("bJ");
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
    public void sendParticle(Player player, String particle, double x, double y, double z, int amount) {
        player.spawnParticle(Particle.valueOf(particle.toUpperCase().trim()), x, y, z, amount);
    }

    @Override
    public void playSound(Player player, Sound sound, double x, double y, double z, float volume, float pitch) {
        PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(new MinecraftKey(sound.getKey().getKey()), SoundCategory.MASTER, new Vec3D(player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ()), volume, pitch);
        CraftPlayer cp = (CraftPlayer) player;
        EntityPlayer cep = cp.getHandle();
        cep.playerConnection.sendPacket(packet);
    }

    @Override
    public void rickroll(Player player, @Nullable CommandSender sender) {
        try {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(new MinecraftKey("music.rickroll"), SoundCategory.MASTER, new Vec3D(player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ()), Float.MAX_VALUE, 1f);
            connection.sendPacket(packet);
            if (sender != null) sender.sendMessage(CC.tf("&e%s just got rickrolled!", player.getName()));
            EntityPlayer npc = new EntityPlayer(((CraftPlayer) player).getHandle().server, ((CraftPlayer) player).getHandle().getWorldServer(), RickrollCommand.rickastley, new PlayerInteractManager(((CraftPlayer) player).getHandle().getWorldServer()));
            Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(2.5));
            Location npcloc = loc.setDirection(player.getLocation().subtract(loc).toVector());
            float yaw = npcloc.getYaw();
            float pitch = npcloc.getPitch();
            npc.setLocation(npcloc.getX(), npcloc.getY(), npcloc.getZ(), npcloc.getYaw(), npcloc.getPitch());
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) ((yaw % 360.) * 256 / 360), (byte) ((pitch % 360.) * 256 / 360), false));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) ((yaw % 360.) * 256 / 360)));
            RickrollCommand.entities.put(player.getUniqueId(), npc);
            plugin.getServer().getPluginManager().callEvent(new PlayerRickrolledEvent(sender, player, npc));
            new BukkitRunnable() {
                @Override
                public void run() {
                    RickrollCommand.requested.remove(player.getUniqueId());
                    RickrollCommand.entities.remove(player.getUniqueId());
                    try {
                        player.stopSound("music.rickroll");
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                        npc.playerConnection.disconnect("");
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
            PlayerConnection connection = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
            npc.setLocation(npcloc.getX(), npcloc.getY(), npcloc.getZ(), npcloc.getYaw(), npcloc.getPitch());
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) ((yaw % 360.) * 256 / 360), (byte) ((pitch % 360.) * 256 / 360), false));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) ((yaw % 360.) * 256 / 360)));
        }
    }

    @Override
    public void handleRickrollQuit(PlayerQuitEvent event) {
        if (RickrollCommand.entities.containsKey(event.getPlayer().getUniqueId())) {
            EntityPlayer npc = (EntityPlayer) RickrollCommand.entities.get(event.getPlayer().getUniqueId());
            RickrollCommand.requested.remove(event.getPlayer().getUniqueId());
            RickrollCommand.entities.remove(event.getPlayer().getUniqueId());
            try {
                ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
                ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                npc.playerConnection.disconnect("");
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
