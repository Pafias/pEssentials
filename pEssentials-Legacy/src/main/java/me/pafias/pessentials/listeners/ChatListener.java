package me.pafias.pessentials.listeners;

import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.RandomUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.stream.Collectors;

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
            event.getRecipients().forEach(recipient -> recipient.sendMessage(msg));
            plugin.getServer().getConsoleSender().sendMessage(msg);
        }
    }

    @EventHandler
    public void onBlockedChat(AsyncPlayerChatEvent event) {
        event.getRecipients().removeAll(
                plugin.getSM().getUserManager().getUsers().values()
                        .stream()
                        .filter(u -> u.getBlocking().contains(event.getPlayer().getUniqueId()) && !event.getPlayer().hasPermission("essentials.block.bypass"))
                        .map(User::getPlayer)
                        .collect(Collectors.toSet())
        );
    }

}
