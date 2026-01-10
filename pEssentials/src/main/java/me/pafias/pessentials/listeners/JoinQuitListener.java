package me.pafias.pessentials.listeners;

import me.pafias.pessentials.pEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final pEssentials plugin;

    public JoinQuitListener(pEssentials plugin) {
        this.plugin = plugin;
        joinMessageEnabled = plugin.getConfig().getBoolean("join_message_enabled");
        quitMessageEnabled = plugin.getConfig().getBoolean("quit_message_enabled");
    }

    private final boolean joinMessageEnabled, quitMessageEnabled;

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        plugin.getSM().getUserManager().addUser(event.getPlayer());
        if (!joinMessageEnabled)
            event.setJoinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        if (!quitMessageEnabled)
            event.setQuitMessage(null);
        plugin.getSM().getUserManager().removeUser(event.getPlayer());
    }

}
