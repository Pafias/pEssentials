package me.pafias.pafiasessentials.listeners;

import me.pafias.pafiasessentials.commands.KnockbackCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class KnockbackListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        double x = KnockbackCommand.x;
        double y = KnockbackCommand.y;
        double z = KnockbackCommand.z;
        if (x == 1 && y == 1 && z == 1) return;
        event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(new Vector(x, y, z)));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVelocity(PlayerVelocityEvent event) {
        if (event.isCancelled()) return;
        double x = KnockbackCommand.x;
        double y = KnockbackCommand.y;
        double z = KnockbackCommand.z;
        if (x == 1 && y == 1 && z == 1) return;
        event.getPlayer().setVelocity(event.getVelocity().multiply(new Vector(x, y, z)));
    }

}
