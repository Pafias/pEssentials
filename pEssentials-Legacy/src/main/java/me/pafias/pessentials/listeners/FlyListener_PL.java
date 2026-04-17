package me.pafias.pessentials.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.UserManager;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class FlyListener_PL extends PacketAdapter implements Listener {

    public FlyListener_PL(final pEssentials ae) {
        super(ae, com.comphenix.protocol.PacketType.Play.Client.STEER_VEHICLE);
        userManager = ae.getSM().getUserManager();
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    private final UserManager userManager;

    @Override
    public void onPacketReceiving(PacketEvent event) {
        try {
            final PacketContainer packet = event.getPacket();
            final User user = userManager.getUser(event.getPlayer());
            if (user == null || !user.flyingEntity)
                return;
            final Entity entity = user.getPlayer().getVehicle();
            if (entity == null)
                return;
            event.setCancelled(true);
            final float orientation = packet.getFloat().read(1);
            final boolean sneak = packet.getBooleans().read(0);
            final Vector direction = user.getPlayer().getLocation().getDirection();
            if (orientation > 0) {
                final Vector v = entity.getVelocity();
                v.zero();
                v.add(direction);
                v.multiply(1);
                entity.setVelocity(v);
            }
            if (orientation < 0) {
                final Vector v = entity.getVelocity();
                v.zero();
                v.add(direction);
                v.multiply(-1);
                entity.setVelocity(v);
            }
            if (sneak)
                user.flyingEntity = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
