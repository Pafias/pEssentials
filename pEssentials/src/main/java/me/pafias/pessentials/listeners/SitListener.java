package me.pafias.pessentials.listeners;

import me.pafias.pessentials.commands.modules.SitCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class SitListener implements Listener {

    @EventHandler
    public void onStand(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof HumanEntity human)) return;
        final ArmorStand as = SitCommand.map.get(human);
        if (as == null || event.getDismounted() != as) return;
        as.remove();
        SitCommand.map.remove(human);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (!event.getEntity().isInsideVehicle()) return;
        if (!(event.getEntity() instanceof HumanEntity human)) return;
        final ArmorStand as = SitCommand.map.remove(human);
        if (as == null) return;
        as.remove();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().isInsideVehicle()) return;
        final ArmorStand as = SitCommand.map.remove(event.getPlayer());
        if (as == null) return;
        as.remove();
    }

}
