package me.pafias.pafiasessentials.listeners;

import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final PafiasEssentials plugin;

    public JoinQuitListener(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        plugin.getSM().getUserManager().addUser(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        if (!plugin.getSM().getVariables().joinMessage)
            event.setJoinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        if (!plugin.getSM().getVariables().quitMessage)
            event.setQuitMessage(null);
        plugin.getSM().getUserManager().removeUser(event.getPlayer());
    }

}
