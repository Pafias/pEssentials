package me.pafias.pessentials.listeners;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Tasks;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.RegEx;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class BadPeopleListener implements Listener {

    private final pEssentials plugin;

    private final ConfigurationSection config;

    public BadPeopleListener(pEssentials plugin) {
        this.plugin = plugin;
        config = plugin.getConfig().getConfigurationSection("bad_people_filter");
        MAX_MESSAGES = config.getInt("anti_spam.max_messages", 12);
        TIME_PERIOD = config.getInt("anti_spam.time_period_millis", 5000);
        MUTE_DURATION = config.getInt("anti_spam.mute_duration_millis", 10000);
        usernameRegex = config.getString("username_regex");
        regex1 = config.getString("anti_advertising.regex1");
        regex2 = config.getString("anti_advertising.regex2");
    }

    @RegEx
    private final String usernameRegex;
    @RegEx
    private final String regex1;
    @RegEx
    private final String regex2;

    private final Map<UUID, BukkitTask> toVerify = new HashMap<>();

    private final Map<UUID, PlayerChatData> chatData = new HashMap<>();

    private int MAX_MESSAGES = 12;
    private long TIME_PERIOD = 5000;
    private long MUTE_DURATION = 10000;

    private boolean isValidUsername(String username) {
        if (username.length() < 2 || username.length() > 16)
            return false;
        return username.matches(usernameRegex);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        final UUID playerUUID = event.getUniqueId();
        final String playerName = event.getName();

        if (config.getBoolean("username_check")) {
            if (!isValidUsername(playerName)) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Invalid username. Your name must be 3-16 characters long and contain only letters, numbers, and underscores.");
                plugin.getLogger().log(Level.INFO, String.format("%s attempted to join with the invalid username %s", event.getAddress().getHostAddress(), playerName));
                return;
            }
        }

        if (config.getBoolean("movement_verification")) {
            final BukkitTask verificationTask = Tasks.runLaterAsync(30 * 20, () -> {
                if (toVerify.containsKey(playerUUID)) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Failed to verify your account.");
                    plugin.getLogger().log(Level.INFO, String.format("Player %s has been disallowed login due to failed verification.", event.getName()));
                }
                toVerify.remove(playerUUID);
            });
            toVerify.put(playerUUID, verificationTask);
        }
    }

    @EventHandler
    public void onConnectionClose(PlayerConnectionCloseEvent event) {
        toVerify.remove(event.getPlayerUniqueId());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!config.getBoolean("movement_verification")) return;
        final UUID playerUUID = event.getPlayer().getUniqueId();
        final BukkitTask task = toVerify.remove(playerUUID);
        if (task != null) {
            task.cancel();
            plugin.getLogger().log(Level.INFO, String.format("Player %s has been verified by movement.", event.getPlayer().getName()));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();

        // No talking when unverified
        if (toVerify.containsKey(uuid)) {
            event.getRecipients().removeIf(p -> !p.equals(event.getPlayer()));
            plugin.getLogger().log(Level.INFO, String.format("Player %s tried to chat before verification. Message hidden.", event.getPlayer().getName()));
            return;
        }

        // Anti advertising
        if (config.getBoolean("anti_advertising.enabled") && !event.getPlayer().hasPermission("essentials.bypass.advertising")) {
            final String message = event.getMessage().toLowerCase();
            if (message.matches(regex1) ||
                    message.matches(regex2)) {
                if (config.getBoolean("anti_advertising.shadow_disallow")) {
                    event.getRecipients().removeIf(p -> !p.equals(event.getPlayer()));
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.t("&cYou are not allowed to say that"));
                }
                plugin.getLogger().log(Level.INFO, String.format("Player %s attempted to advertise in chat. Message blocked: %s", event.getPlayer().getName(), message));
                return;
            }
        }

        // Anti spam

        if (!config.getBoolean("anti_spam.enabled") || event.getPlayer().hasPermission("essentials.bypass.antispam"))
            return;

        final PlayerChatData data = chatData.getOrDefault(uuid, new PlayerChatData());
        final long currentTime = System.currentTimeMillis();
        data.addMessageTime(currentTime);
        data.cleanupMessages(currentTime - TIME_PERIOD);

        if (data.isMuted()) {
            if (currentTime > data.getMuteEndTime()) {
                data.setMuted(false);
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(CC.t("&cYou are currently on cooldown for spamming."));
                return;
            }
        }

        if (data.getMessageCount() > MAX_MESSAGES) {
            if (config.getBoolean("anti_spam.auto_mute")) {
                data.setMuted(true);
                data.setMuteEndTime(currentTime + MUTE_DURATION);
            }
            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.t("&cYou have been muted for " + (MUTE_DURATION / 1000) + " seconds due to spamming."));
        } else if (data.getMessageCount() == MAX_MESSAGES || data.getMessageCount() == MAX_MESSAGES - 1) {
            event.getPlayer().sendMessage(CC.t("&e&oPlease slow down, you are sending messages too quickly!"));
        }

        chatData.put(uuid, data);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        toVerify.remove(uuid);
        chatData.remove(uuid);
    }

    private static class PlayerChatData {
        private long muteEndTime;
        private boolean muted;
        private final Map<Long, Integer> messageTimestamps;

        public PlayerChatData() {
            this.muted = false;
            this.messageTimestamps = new HashMap<>();
        }

        public void addMessageTime(long time) {
            messageTimestamps.put(time, messageTimestamps.getOrDefault(time, 0) + 1);
        }

        public void cleanupMessages(long threshold) {
            messageTimestamps.keySet().removeIf(time -> time < threshold);
        }

        public int getMessageCount() {
            return messageTimestamps.values().stream().mapToInt(Integer::intValue).sum();
        }

        public boolean isMuted() {
            return muted;
        }

        public void setMuted(boolean muted) {
            this.muted = muted;
        }

        public long getMuteEndTime() {
            return muteEndTime;
        }

        public void setMuteEndTime(long muteEndTime) {
            this.muteEndTime = muteEndTime;
        }
    }

}
