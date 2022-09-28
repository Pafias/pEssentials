package me.pafias.pafiasessentials.listeners;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    private final PafiasEssentials plugin;

    public TeleportListener(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        User user = plugin.getSM().getUserManager().getUser(event.getPlayer());
        if (user == null) return;
        user.lastLocation = event.getFrom();
    }

}
