package me.pafias.pessentials.listeners;

import me.pafias.pessentials.commands.modules.SitCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class SitListener implements Listener {

    @EventHandler
    public void onStand(EntityDismountEvent event) {
        if (SitCommand.map.containsKey(event.getEntity())) {
            ArmorStand as = SitCommand.map.get(event.getEntity());
            if (!event.getDismounted().equals(as)) return;
            as.remove();
            SitCommand.map.remove(event.getEntity());
        }
    }

}
