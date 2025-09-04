package me.pafias.pessentials.listeners;

import me.pafias.pessentials.commands.modules.SitCommand;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class SitListener implements Listener {

    @EventHandler
    public void onStand(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof HumanEntity)) return;
        final HumanEntity human = (HumanEntity) event.getEntity();
        final ExperienceOrb exp = SitCommand.map.get(human);
        if (!event.getDismounted().equals(exp)) return;
        exp.remove();
        SitCommand.map.remove(human);
    }

}
