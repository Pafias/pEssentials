package me.pafias.pessentials.listeners;

import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    private final pEssentials plugin;

    public TeleportListener(pEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        final User user = plugin.getSM().getUserManager().getUser(event.getPlayer());
        if (user == null) return;
        user.lastLocation = event.getFrom();
    }

}
