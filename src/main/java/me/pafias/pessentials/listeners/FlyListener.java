package me.pafias.pessentials.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.objects.User;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class FlyListener implements Listener {

    public FlyListener(final pEssentials ae) {
        try {
            ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ae, PacketType.Play.Client.STEER_VEHICLE) {

                public void onPacketReceiving(PacketEvent event) {
                    try {
                        PacketContainer packet = event.getPacket();
                        User user = ae.getSM().getUserManager().getUser(event.getPlayer());
                        if (user == null || !user.flyingEntity)
                            return;
                        Entity entity = user.getPlayer().getVehicle();
                        if (entity == null)
                            return;
                        event.setCancelled(true);
                        if (packet.getFloat().read(1) > 0) {
                            Vector v = entity.getVelocity();
                            v.zero();
                            v.add(user.getPlayer().getLocation().getDirection());
                            v.multiply(1);
                            entity.setVelocity(v);
                        }
                        if (packet.getFloat().read(1) < 0) {
                            Vector v = entity.getVelocity();
                            v.zero();
                            v.add(user.getPlayer().getLocation().getDirection());
                            v.multiply(-1);
                            entity.setVelocity(v);
                        }
                        if (packet.getBooleans().read(0))
                            user.flyingEntity = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception exception) {
        }
    }

}
