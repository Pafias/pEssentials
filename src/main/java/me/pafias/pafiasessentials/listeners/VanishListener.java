package me.pafias.pafiasessentials.listeners;

import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {

    private final PafiasEssentials plugin;

    public VanishListener(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getSM().getVanishManager().getVanishedPlayers().stream()
                .filter(uuid -> !uuid.equals(event.getPlayer().getUniqueId()))
                .filter(uuid -> plugin.getServer().getPlayer(uuid) != null)
                .forEach(uuid -> {
                    if (!event.getPlayer().hasPermission("essentials.vanish.bypass")) {
                        event.getPlayer().hidePlayer(plugin.getServer().getPlayer(uuid));
                    }
                });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (plugin.getSM().getVanishManager().isVanished(event.getPlayer()))
            plugin.getSM().getVanishManager().unvanish(event.getPlayer());
    }

}
