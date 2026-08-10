package me.pafias.pessentials.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerInput;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.putils.CC;
import me.pafias.putils.Tasks;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlyProtocolListener extends SimplePacketListenerAbstract implements Listener {

    private final pEssentials plugin;

    public FlyProtocolListener(final pEssentials plugin) {
        this.plugin = plugin;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            final User user = plugin.getSM().getUserManager().getUser((Player) event.getEntity());
            if (user != null)
                cleanup(user);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final User user = plugin.getSM().getUserManager().getUser(event.getPlayer());
        if (user != null)
            cleanup(user);
    }

    private void cleanup(@NotNull User user) {
        user.movingEntity = false;
        speedMap.remove(user.getUUID());
        inputMap.remove(user.getUUID());
        final BukkitTask task = taskMap.remove(user.getUUID());
        if (task != null)
            task.cancel();
    }

    private final Map<UUID, Double> speedMap = new HashMap<>();
    private final Map<UUID, BukkitTask> taskMap = new HashMap<>();
    private final Map<UUID, InputState> inputMap = new HashMap<>();

    private static class InputState {
        volatile boolean forward, backward, left, right;
    }

    @Override
    public void onPacketPlayReceive(@NotNull PacketPlayReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_INPUT) {
            try {
                final Entity entity = ((Player) event.getPlayer()).getVehicle();
                if (entity == null)
                    return;

                final User user = plugin.getSM().getUserManager().getUser((Player) event.getPlayer());
                if (user == null || !user.movingEntity)
                    return;

                final WrapperPlayClientPlayerInput packet = new WrapperPlayClientPlayerInput(event);
                event.setCancelled(true);

                if (packet.isShift()) {
                    cleanup(user);
                    return;
                }

                if (!packet.isForward() && !packet.isBackward() && !packet.isRight() && !packet.isLeft()) {
                    final BukkitTask task = taskMap.remove(user.getUUID());
                    if (task != null)
                        task.cancel();
                    inputMap.remove(user.getUUID());
                    entity.setVelocity(new Vector());
                } else {
                    final InputState state = inputMap.computeIfAbsent(user.getUUID(), id -> new InputState());
                    state.forward = packet.isForward();
                    state.backward = packet.isBackward();
                    state.left = packet.isLeft();
                    state.right = packet.isRight();

                    taskMap.computeIfAbsent(user.getUUID(), id -> Tasks.runRepeatingSync(1, 1, () -> {
                        final double speed = speedMap.getOrDefault(user.getUUID(), 0.1d);

                        final Vector direction = user.getPlayer().getLocation().getDirection().normalize();
                        direction.setY(direction.getY() * 0.5);
                        direction.normalize();

                        if (state.forward) {
                            direction.multiply(speed);
                        }
                        if (state.backward) {
                            direction.multiply(-speed);
                        }
                        if (state.right) {
                            double newSpeed = speed + 0.1D;
                            speedMap.put(user.getUUID(), newSpeed);
                            user.getPlayer().sendTitle("", CC.tf("&6Speed: &7%.2f", newSpeed), 2, 10, 5);
                        }
                        if (state.left) {
                            double newSpeed = speed - 0.1D;
                            speedMap.put(user.getUUID(), newSpeed);
                            user.getPlayer().sendTitle("", CC.tf("&6Speed: &7%.2f", newSpeed), 2, 10, 5);
                        }

                        entity.setVelocity(direction);
                    }));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
