package me.pafias.pessentials.listeners;

import me.pafias.pessentials.pEssentials;
import me.pafias.putils.CC;
import me.pafias.putils.LCC;
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
        firstTimeJoinMessage = plugin.getConfig().getString("join_message_first_time");
        quitMessageEnabled = plugin.getConfig().getBoolean("quit_message_enabled");
        quitMessage = plugin.getConfig().getString("quit_message");
    }

    private final boolean joinMessageEnabled, quitMessageEnabled;
    private final String joinMessage, firstTimeJoinMessage, quitMessage;

    @EventHandler(priority = EventPriority.LOW)
    public void addUser(PlayerJoinEvent event) {
        plugin.getSM().getUserManager().addUser(event.getPlayer());
    }

    @EventHandler
    public void joinMessage(PlayerJoinEvent event) {
        if (!joinMessageEnabled) {
            event.setJoinMessage(null);
            return;
        }
        if (firstTimeJoinMessage != null && !event.getPlayer().hasPlayedBefore() && !firstTimeJoinMessage.isEmpty()) {
            try {
                event.joinMessage(CC.af(firstTimeJoinMessage, event.getPlayer().getName()));
            } catch (Throwable t) {
                event.setJoinMessage(LCC.tf(firstTimeJoinMessage, event.getPlayer().getName()));
            }
        } else if (joinMessage != null && !joinMessage.isEmpty()) {
            try {
                event.joinMessage(CC.af(joinMessage, event.getPlayer().getName()));
            } catch (Throwable t) {
                event.setJoinMessage(LCC.tf(joinMessage, event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void quitMessage(PlayerQuitEvent event) {
        if (!quitMessageEnabled)
            event.setQuitMessage(null);
        else if (quitMessage != null && !quitMessage.trim().isEmpty())
            try {
                event.quitMessage(CC.af(quitMessage, event.getPlayer().getName()));
            } catch (Throwable t) {
                event.setQuitMessage(LCC.tf(quitMessage, event.getPlayer().getName()));
            }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void removeUser(PlayerQuitEvent event) {
        plugin.getSM().getUserManager().removeUser(event.getPlayer());
    }

}
