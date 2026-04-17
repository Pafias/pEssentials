package me.pafias.pessentials.listeners;

import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.UserManager;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final UserManager userManager;

    public ChatListener(pEssentials plugin, UserManager userManager) {
        this.userManager = userManager;
        staffchatFormat = plugin.getConfig().getString("staffchat_format");
    }

    private static final String PERMISSION = "essentials.staffchat";
    private final String staffchatFormat;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStaffChat(AsyncPlayerChatEvent event) {
        final User user = userManager.getUser(event.getPlayer());
        if (user == null) return;
        final String message = event.getMessage();
        final boolean startsWithEMark = message.startsWith("!");
        final boolean startsWithHashtag = message.startsWith("#");
        if ((user.isInStaffchat() && startsWithEMark) || (startsWithHashtag && event.getPlayer().hasPermission(PERMISSION)))
            if (user.isInStaffchat() && startsWithEMark)
                event.setMessage(message.substring(1));
            else if (startsWithHashtag && event.getPlayer().hasPermission(PERMISSION))
                event.setMessage(message.substring(1));
        if ((user.isInStaffchat() && !startsWithEMark) || (startsWithHashtag && event.getPlayer().hasPermission(PERMISSION))) {
            final String format = staffchatFormat
                    .replace("{player}", user.getPlayer().getName())
                    .replace("{message}", event.getMessage());
            event.setFormat(LCC.t(format));
            event.getRecipients().clear();
            event.getRecipients().addAll(RandomUtils.getStaffOnline(PERMISSION));

            // Because of DiscordSRV (and maybe other plugins), we gotta use this workaround
            event.setCancelled(true);
            final String msg = String.format(event.getFormat(), event.getPlayer().getName(), event.getMessage());
            event.getRecipients().forEach(recipient -> recipient.sendMessage(msg));
            Bukkit.getConsoleSender().sendMessage(msg);
        }
    }

    @EventHandler
    public void onBlockedChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("essentials.block.bypass")) {
            for (final User user : userManager.getUsers().values()) {
                if (user.getBlocking().contains(event.getPlayer().getUniqueId())) {
                    event.getRecipients().remove(user.getPlayer());
                }
            }
        }
    }

}
