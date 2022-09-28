package me.pafias.pafiasessentials.listeners;

import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getSM().getLabymodManager().removeUser(event.getPlayer());
        plugin.getSM().getUserManager().removeUser(event.getPlayer());
    }

}
