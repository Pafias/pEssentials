package me.pafias.pessentials.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MoveListener implements Listener {

    private final Map<UUID, Double> speed = new HashMap<>();

    public MoveListener(final pEssentials ae) {
        try {
            PacketEvents.getAPI().getEventManager().registerListener(new SimplePacketListenerAbstract() {
                @Override
                public void onPacketPlayReceive(@NotNull PacketPlayReceiveEvent event) {
                    if (!event.getPacketType().equals(PacketType.Play.Client.STEER_VEHICLE)) return;
                    try {
                        final User user = ae.getSM().getUserManager().getUser((Player) event.getPlayer());
                        if (user == null || !user.movingEntity)
                            return;
                        final Entity entity = user.getPlayer().getVehicle();
                        if (entity == null)
                            return;
                        final WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(event);
                        event.setCancelled(true);
                        if (!speed.containsKey(user.getUUID()))
                            speed.put(user.getUUID(), 0.1D);
                        final double i = speed.get(user.getUUID());
                        Vector v = new Vector();
                        if (packet.getForward() > 0) {
                            v = user.getPlayer().getLocation().getDirection();
                            v.multiply(i);
                        }
                        if (packet.getForward() < 0) {
                            v = user.getPlayer().getLocation().getDirection();
                            v.multiply(-i);
                        }
                        if (packet.getSideways() < 0) {
                            double ii = i + 0.1D;
                            speed.replace(user.getUUID(), ii);
                            user.getPlayer().sendTitle("", CC.tf("&6Speed: &7%.2f", ii), 2, 10, 5);
                        }
                        if (packet.getSideways() > 0) {
                            double ii = i - 0.1D;
                            speed.replace(user.getUUID(), ii);
                            user.getPlayer().sendTitle("", CC.tf("&6Speed: &7%.2f", ii), 2, 10, 5);
                        }
                        entity.setVelocity(v);
                        if (packet.isUnmount())
                            user.movingEntity = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Throwable ignored) {
        }
    }

}
