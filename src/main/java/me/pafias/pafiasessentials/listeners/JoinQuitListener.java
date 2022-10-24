package me.pafias.pafiasessentials.listeners;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinQuitListener implements Listener {

    private final PafiasEssentials plugin;

    public JoinQuitListener(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        int ping = plugin.getSM().getNMSProvider().getPing(event.getPlayer());
        if (ping > plugin.getSM().getVariables().pingKickThreshold)
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.tf("&cYour ping is too high. (%dms)", ping));
        plugin.getSM().getUserManager().addUser(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        if (!plugin.getSM().getVariables().joinMessage)
            event.setJoinMessage(null);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getPlayer() != null) {
                    int ping = plugin.getSM().getNMSProvider().getPing(event.getPlayer());
                    if (ping > plugin.getSM().getVariables().pingKickThreshold)
                        event.getPlayer().kickPlayer(CC.tf("&cYour ping is too high. (%dms)", ping));
                }
            }
        }.runTaskLater(plugin, 5 * 20);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        if (!plugin.getSM().getVariables().quitMessage)
            event.setQuitMessage(null);
        plugin.getSM().getLabymodManager().removeUser(event.getPlayer());
        plugin.getSM().getUserManager().removeUser(event.getPlayer());
    }

}
