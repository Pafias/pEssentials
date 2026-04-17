package me.pafias.pessentials.listeners;

import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.services.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    private final UserManager userManager;

    public TeleportListener(UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        final User user = userManager.getUser(event.getPlayer());
        if (user == null) return;
        user.lastLocation = event.getFrom();
    }

}
