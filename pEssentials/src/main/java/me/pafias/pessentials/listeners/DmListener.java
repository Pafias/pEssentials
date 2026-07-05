package me.pafias.pessentials.listeners;

import me.pafias.pessentials.commands.modules.TellCommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.services.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DmListener implements Listener {

    private final UserManager userManager;

    public DmListener(UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final User user = userManager.getUser(event.getPlayer());
        TellCommand.msg.remove(user);
    }

}
