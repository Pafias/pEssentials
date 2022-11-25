package me.pafias.pafiasessentials.services;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FreezeManager implements Listener {

    private final PafiasEssentials plugin;

    public FreezeManager(PafiasEssentials plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        frozen = new HashSet<>();
    }

    private final Set<UUID> frozen;

    public Set<UUID> getFrozenUsers() {
        return frozen;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!frozen.contains(event.getPlayer().getUniqueId())) return;
        if (event.getTo().getBlockX() != event.getFrom().getBlockX()
                || event.getTo().getBlockY() != event.getFrom().getBlockY()
                || event.getTo().getBlockZ() != event.getFrom().getBlockZ()) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        if (frozen.contains(event.getPlayer().getUniqueId()))
            plugin.getServer().broadcastMessage(CC.tf("&d&l%s &c&llogged out while frozen.", event.getPlayer().getName()));
    }

}
