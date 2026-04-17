package me.pafias.pessentials.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.UserManager;
import me.pafias.putils.LCC;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MoveListener_PL extends PacketAdapter implements Listener {

    public MoveListener_PL(final pEssentials ae) {
        super(ae, com.comphenix.protocol.PacketType.Play.Client.STEER_VEHICLE);
        userManager = ae.getSM().getUserManager();
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    private final UserManager userManager;

    private final Map<UUID, Double> speedMap = new HashMap<>();

    @Override
    public void onPacketReceiving(PacketEvent event) {
        try {
            final User user = userManager.getUser(event.getPlayer());
            if (user == null || !user.movingEntity)
                return;
            final Entity entity = user.getPlayer().getVehicle();
            if (entity == null)
                return;
            final PacketContainer packet = event.getPacket();
            event.setCancelled(true);
            if (!speedMap.containsKey(user.getUUID()))
                speedMap.put(user.getUUID(), 0.1D);
            final double speed = this.speedMap.get(user.getUUID());
            Vector v = new Vector();
            final float orientationFwBw = packet.getFloat().read(1);
            final float orientationSides = packet.getFloat().read(0);
            final boolean sneak = packet.getBooleans().read(0);
            final Vector direction = user.getPlayer().getLocation().getDirection();
            final DecimalFormat decimalFormat = new DecimalFormat("#.##");
            if (orientationFwBw > 0) {
                v = direction;
                v.multiply(speed);
            }
            if (orientationFwBw < 0) {
                v = direction;
                v.multiply(-speed);
            }
            if (orientationSides < 0) {
                double newSpeed = speed + 0.1D;
                this.speedMap.replace(user.getUUID(), newSpeed);
                user.getPlayer().sendTitle("", LCC.t("&6Speed: &7" + decimalFormat.format(newSpeed)), 2, 10, 5);
            }
            if (orientationSides > 0) {
                double ii = speed - 0.1D;
                this.speedMap.replace(user.getUUID(), ii);
                user.getPlayer().sendTitle("", LCC.t("&6Speed: &7" + decimalFormat.format(ii)), 2, 10, 5);
            }
            entity.setVelocity(v);
            if (sneak)
                user.movingEntity = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
