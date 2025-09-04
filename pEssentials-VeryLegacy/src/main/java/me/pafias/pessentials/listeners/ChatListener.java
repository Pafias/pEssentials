package me.pafias.pessentials.listeners;

import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.RandomUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final pEssentials plugin;

    public ChatListener(pEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStaffChat(AsyncPlayerChatEvent event) {
        final User user = plugin.getSM().getUserManager().getUser(event.getPlayer());
        if (user == null) return;
        final String message = event.getMessage();
        if ((user.isInStaffchat() && message.startsWith("!")) || (message.startsWith("#") && event.getPlayer().hasPermission("essentials.staffchat")))
            if (user.isInStaffchat() && message.startsWith("!"))
                event.setMessage(message.substring(1));
            else if (message.startsWith("#") && event.getPlayer().hasPermission("essentials.staffchat"))
                event.setMessage(message.substring(1));
        if ((user.isInStaffchat() && !message.startsWith("!")) || (message.startsWith("#") && event.getPlayer().hasPermission("essentials.staffchat"))) {
            event.setFormat(CC.formatStaffchat(user.getPlayer().getName(), event.getMessage()));
            event.getRecipients().clear();
            event.getRecipients().addAll(RandomUtils.getStaffOnline("essentials.staffchat"));

            // Because of DiscordSRV (and maybe other plugins), we gotta use this workaround
            event.setCancelled(true);
            String msg = String.format(event.getFormat(), event.getPlayer().getName(), event.getMessage());
            for (Player recipient : event.getRecipients()) {
                recipient.sendMessage(msg);
            }
            plugin.getServer().getConsoleSender().sendMessage(msg);
        }
    }

    @EventHandler
    public void onBlockedChat(AsyncPlayerChatEvent event) {
        for (Player p : event.getRecipients()) {
            for (User u : plugin.getSM().getUserManager().getUsers().values()) {
                if (u.getBlocking().contains(event.getPlayer().getName()) && !event.getPlayer().hasPermission("essentials.block.bypass")) {
                    event.getRecipients().remove(p);
                    break;
                }
            }
        }
    }

}
