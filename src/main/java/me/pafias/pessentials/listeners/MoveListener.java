package me.pafias.pessentials.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MoveListener implements Listener {

    private final Map<UUID, Double> speed = new HashMap<>();

    public MoveListener(final pEssentials ae) {
        try {
            ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ae, PacketType.Play.Client.STEER_VEHICLE) {

                public void onPacketReceiving(PacketEvent event) {
                    try {
                        User user = ae.getSM().getUserManager().getUser(event.getPlayer());
                        if (user == null || !user.movingEntity)
                            return;
                        Entity entity = user.getPlayer().getVehicle();
                        if (entity == null)
                            return;
                        PacketContainer packet = event.getPacket();
                        event.setCancelled(true);
                        if (!speed.containsKey(user.getUUID()))
                            speed.put(user.getUUID(), 0.1D);
                        double i = speed.get(user.getUUID());
                        Vector v = new Vector();
                        if (packet.getFloat().read(1) > 0) {
                            v = user.getPlayer().getLocation().getDirection();
                            v.multiply(i);
                        }
                        if (packet.getFloat().read(1) < 0) {
                            v = user.getPlayer().getLocation().getDirection();
                            v.multiply(-i);
                        }
                        if (packet.getFloat().read(0) < 0) {
                            double ii = i + 0.1D;
                            speed.replace(user.getUUID(), ii);
                            DecimalFormat df = new DecimalFormat("#.##");
                            user.getPlayer().sendTitle("", CC.t("&6Speed: &7" + df.format(ii)), 2, 10, 5);
                        }
                        if (packet.getFloat().read(0) > 0) {
                            double ii = i - 0.1D;
                            speed.replace(user.getUUID(), ii);
                            DecimalFormat df = new DecimalFormat("#.##");
                            user.getPlayer().sendTitle("", CC.t("&6Speed: &7" + df.format(ii)), 2, 10, 5);
                        }
                        entity.setVelocity(v);
                        if(packet.getBooleans().read(0))
                            user.movingEntity = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

}
