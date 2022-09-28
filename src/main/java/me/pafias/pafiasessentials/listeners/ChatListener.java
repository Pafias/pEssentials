package me.pafias.pafiasessentials.listeners;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import me.pafias.pafiasessentials.util.RandomUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final PafiasEssentials plugin;

    public ChatListener(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        User user = plugin.getSM().getUserManager().getUser(event.getPlayer());
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
