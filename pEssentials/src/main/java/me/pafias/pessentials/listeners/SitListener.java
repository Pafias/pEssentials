package me.pafias.pessentials.listeners;

import me.pafias.pessentials.commands.modules.Staff.Fun.SitCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

}
