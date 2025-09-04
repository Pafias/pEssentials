package me.pafias.pessentials.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class FlyListener implements Listener {

    public FlyListener(final pEssentials ae) {
        try {
            PacketEvents.getAPI().getEventManager().registerListener(new SimplePacketListenerAbstract() {
                @Override
                public void onPacketPlayReceive(@NotNull PacketPlayReceiveEvent event) {
                    if (!event.getPacketType().equals(PacketType.Play.Client.STEER_VEHICLE)) return;
                    try {
                        final WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(event);
                        final User user = ae.getSM().getUserManager().getUser((Player) event.getPlayer());
                        if (user == null || !user.flyingEntity)
                            return;
                        final Entity entity = user.getPlayer().getVehicle();
                        if (entity == null)
                            return;
                        event.setCancelled(true);
                        if (packet.getForward() > 0) {
                            final Vector v = entity.getVelocity();
                            v.zero();
                            v.add(user.getPlayer().getLocation().getDirection());
                            v.multiply(1);
                            entity.setVelocity(v);
                        }
                        if (packet.getForward() < 0) {
                            final Vector v = entity.getVelocity();
                            v.zero();
                            v.add(user.getPlayer().getLocation().getDirection());
                            v.multiply(-1);
                            entity.setVelocity(v);
                        }
                        if (packet.isUnmount())
                            user.flyingEntity = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Throwable ignored) {
        }
    }

}
