package me.pafias.pessentials.listeners;

import me.pafias.pessentials.pEssentials;
import me.pafias.putils.CC;
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
        joinMessage = plugin.getConfig().getString("join_message");
        quitMessageEnabled = plugin.getConfig().getBoolean("quit_message_enabled");
        quitMessage = plugin.getConfig().getString("quit_message");
    }

    private final boolean joinMessageEnabled, quitMessageEnabled;
    private final String joinMessage, quitMessage;

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        plugin.getSM().getUserManager().addUser(event.getPlayer());
        if (!joinMessageEnabled)
            event.setJoinMessage(null);
        else
            try {
                event.joinMessage(CC.a(joinMessage));
            } catch (Throwable t) {
                event.setJoinMessage(CC.t(joinMessage));
            }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        if (!quitMessageEnabled)
            event.setQuitMessage(null);
        else
            try {
                event.quitMessage(CC.a(quitMessage));
            } catch (Throwable t) {
                event.setQuitMessage(CC.t(quitMessage));
            }
        plugin.getSM().getUserManager().removeUser(event.getPlayer());
    }

}
