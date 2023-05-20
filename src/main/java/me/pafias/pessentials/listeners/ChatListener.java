package me.pafias.pessentials.listeners;

import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.RandomUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final pEssentials plugin;

    public ChatListener(pEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStaffChat(AsyncPlayerChatEvent event) {
        User user = plugin.getSM().getUserManager().getUser(event.getPlayer());
        if (user == null) return;
        String message = event.getMessage();
        if ((user.isInStaffChat() && message.startsWith("!")) || (message.startsWith("#") && event.getPlayer().hasPermission("essentials.staffchat")))
            if (user.isInStaffChat() && message.startsWith("!"))
                event.setMessage(message.substring(1));
            else if (message.startsWith("#") && event.getPlayer().hasPermission("essentials.staffchat"))
                event.setMessage(message.substring(1));
        if ((user.isInStaffChat() && !message.startsWith("!")) || (message.startsWith("#") && event.getPlayer().hasPermission("essentials.staffchat"))) {
            event.getRecipients().clear();
            event.getRecipients().addAll(RandomUtils.getStaffOnline("essentials.staffchat"));
            event.setFormat(CC.formatStaffchat(user.getPlayer().getName(), event.getMessage()));
        }
    }

}
