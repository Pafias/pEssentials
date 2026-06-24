package me.pafias.pessentials.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.Getter;
import lombok.Setter;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.pessentials.util.TextUtils;
import me.pafias.putils.CC;
import me.pafias.putils.Tasks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.RegEx;
import java.util.*;
import java.util.logging.Level;

public class BadPeopleListener implements Listener {

    private final pEssentials plugin;

    public BadPeopleListener(pEssentials plugin) {
        this.plugin = plugin;

        final ConfigurationSection config = plugin.getConfig().getConfigurationSection("bad_people_filter");
        usernameCheck = config.getBoolean("username_check");
        movementVerification = config.getBoolean("movement_verification");
        antiAdvertisingEnabled = config.getBoolean("anti_advertising.enabled");
        antiAdvertisingShadowDisallow = config.getBoolean("anti_advertising.shadow_disallow");
        antiSpamEnabled = config.getBoolean("anti_spam.enabled");
        antiSpamAutoMute = config.getBoolean("anti_spam.auto_mute");

        MAX_MESSAGES = config.getInt("anti_spam.max_messages", 12);
        TIME_PERIOD = config.getInt("anti_spam.time_period_millis", 5000);
        MUTE_DURATION = config.getInt("anti_spam.mute_duration_millis", 10000);
        usernameRegex = config.getString("username_regex");
        regex1 = config.getString("anti_advertising.regex1");
        regex2 = config.getString("anti_advertising.regex2");

        chatFilterEnabled = config.getBoolean("chat_filter.enabled");
        chatFilterShadowDisallow = config.getBoolean("chat_filter.shadow_disallow");
        chatFilterNotifyStaff = config.getBoolean("chat_filter.notify_staff");
        blacklistedWords = config.getStringList("chat_filter.blacklisted_words")
                .stream()
                .map(word -> new TextUtils.BlacklistedWord(
                        word,
                        TextUtils.normalize(word)
                ))
                .filter(word -> !word.normalized().isBlank())
                .toList();
    }

    private final boolean usernameCheck;
    private final boolean movementVerification;
    private final boolean antiAdvertisingEnabled, antiAdvertisingShadowDisallow;
    private final boolean antiSpamEnabled, antiSpamAutoMute;

    @RegEx
    private final String usernameRegex;
    @RegEx
    private final String regex1;
    @RegEx
    private final String regex2;

    private final boolean chatFilterEnabled, chatFilterShadowDisallow, chatFilterNotifyStaff;
    private final List<TextUtils.BlacklistedWord> blacklistedWords;

    private final Map<UUID, BukkitTask> toVerify = new HashMap<>();

    private final Map<UUID, PlayerChatData> chatData = new HashMap<>();

    private int MAX_MESSAGES = 12;
    private long TIME_PERIOD = 5000;
    private long MUTE_DURATION = 10000;
    private static final String PERMISSION = "essentials.staffchat";

    @EventHandler
    public void onProfanity(AsyncChatEvent event) {
        if (!chatFilterEnabled) return;

        final Player player = event.getPlayer();

        if (player.hasPermission("essentials.bypass.chatfilter"))
            return;

        final String original = PlainTextComponentSerializer.plainText().serialize(event.message());
        final String normalized = TextUtils.normalize(original);

        TextUtils.BlacklistedWord match = null;

        final String rawLower = original.toLowerCase(Locale.ROOT);
        for (TextUtils.BlacklistedWord word : blacklistedWords) {
            if (normalized.contains(word.normalized()) || rawLower.contains(word.normalized())) {
                match = word;
                break;
            }
        }

        if (match != null) {
            if (chatFilterShadowDisallow) {
                event.viewers().removeIf(p -> !p.equals(player));
            } else {
                event.setCancelled(true);
                player.sendMessage(CC.t("&cYou cannot say that!"));
            }
            if (chatFilterNotifyStaff) {
                Bukkit.getConsoleSender().sendMessage(CC.t("&cPlayer &b" + player.getName() + " &ctried using a blacklisted word: &e" + match.original() + " &7&o(" + original + ")"));
                for (Player staff : RandomUtils.getStaffOnline(PERMISSION)) {
                    staff.sendMessage(
                            CC.a("&cPlayer &b" + player.getName() + " &ctried using a blacklisted word: ")
                                    .append(
                                            CC.a("&e" + match.original())
                                                    .hoverEvent(HoverEvent.showText(Component.text(original)))
                                    )
                    );
                }
            }
        }
    }

    private boolean isValidUsername(String username) {
        if (username.length() < 2 || username.length() > 16)
            return false;
        return username.matches(usernameRegex);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (usernameCheck) {
            final String playerName = event.getName();
            if (!isValidUsername(playerName)) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Invalid username. Your name must be 3-16 characters long and contain only letters, numbers, and underscores.");
                plugin.getLogger().log(Level.INFO, String.format("%s attempted to join with the invalid username %s", event.getAddress().getHostAddress(), playerName));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        if (movementVerification) {
            final UUID playerUUID = event.getPlayer().getUniqueId();
            final String playerName = event.getPlayer().getName();
            final BukkitTask verificationTask = Tasks.runLaterAsync(30 * 20, () -> {
                if (toVerify.containsKey(playerUUID)) {
                    event.getPlayer().kickPlayer(CC.t("&cFailed to verify your account."));
                    plugin.getLogger().log(Level.INFO, String.format("Player %s has been disallowed login due to failed verification.", playerName));
                }
                toVerify.remove(playerUUID);
            });
            toVerify.put(playerUUID, verificationTask);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!movementVerification) return;
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
        if (antiAdvertisingEnabled && !event.getPlayer().hasPermission("essentials.bypass.advertising")) {
            final String message = event.getMessage().toLowerCase();
            if (message.matches(regex1) ||
                    message.matches(regex2)) {
                if (antiAdvertisingShadowDisallow) {
                    event.getRecipients().removeIf(p -> !p.equals(event.getPlayer()));
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.t("&cYou are not allowed to say that"));
                }
                plugin.getLogger().info(String.format("Player %s attempted to advertise in chat. Message blocked: %s", event.getPlayer().getName(), message));
                return;
            }
        }

        // Anti spam

        if (!antiSpamEnabled || event.getPlayer().hasPermission("essentials.bypass.antispam"))
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
            if (antiSpamAutoMute) {
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

        @Setter
        @Getter
        private long muteEndTime;

        @Setter
        @Getter
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

    }

}
