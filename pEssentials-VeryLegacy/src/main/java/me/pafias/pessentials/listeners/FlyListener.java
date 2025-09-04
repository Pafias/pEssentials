package me.pafias.pessentials.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.UserManager;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class FlyListener extends PacketAdapter {

    public FlyListener(pEssentials plugin, UserManager userManager) {
        super(plugin, PacketType.Play.Client.STEER_VEHICLE);
        this.userManager = userManager;
    }

    private final UserManager userManager;

    @Override
    public void onPacketReceiving(PacketEvent event) {
        try {
            final PacketContainer packet = event.getPacket();
            final User user = userManager.getUser(event.getPlayer());
            if (user == null || !user.flyingEntity) return;
            final Entity entity = user.getPlayer().getVehicle();
            if (entity == null) return;
            event.setCancelled(true);
            if (packet.getFloat().read(1) > 0) {
                final Vector v = entity.getVelocity();
                v.zero();
                v.add(user.getPlayer().getLocation().getDirection());
                v.multiply(1);
                entity.setVelocity(v);
            }
            if (packet.getFloat().read(1) < 0) {
                final Vector v = entity.getVelocity();
                v.zero();
                v.add(user.getPlayer().getLocation().getDirection());
                v.multiply(-1);
                entity.setVelocity(v);
            }
            if (packet.getBooleans().read(0)) user.flyingEntity = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
