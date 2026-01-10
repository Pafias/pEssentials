package me.pafias.pessentials.services;

import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class FreezeManager implements Listener {

    private final pEssentials plugin;

    public FreezeManager(pEssentials plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // Frozen Players
    private final Set<UUID> frozen = new HashSet<>();


    // Additional Enforcement Data
    private final Map<UUID, BukkitTask> titleTasks = new HashMap<>();
    private final Map<UUID, ItemStack> helmetCache = new HashMap<>();

    private static final String HELMET_NAME = CC.t("&cFrozen");

    public Set<UUID> getFrozenUsers() {
        return frozen;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!frozen.contains(event.getPlayer().getUniqueId())) return;
        if (event.getTo().getBlockX() != event.getFrom().getBlockX()
                || event.getTo().getBlockY() != event.getFrom().getBlockY()
                || event.getTo().getBlockZ() != event.getFrom().getBlockZ()) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        if (frozen.contains(event.getPlayer().getUniqueId()))
            plugin.getServer().broadcastMessage(CC.tf("\n&d&l%s &c&llogged out while frozen.", event.getPlayer().getName()) + "\n");
    }


    // ====================================================
    // Additional Enforcement
    // (NEW) Display extra Information to the frozen player.
    // ====================================================
    public void applyFrozenExtras(Player player) {
        UUID uuid = player.getUniqueId();
        if (frozen.contains(uuid)) return;

        frozen.add(uuid);

        if (!helmetCache.containsKey(uuid)) {
            ItemStack current = player.getInventory().getHelmet();
            helmetCache.put(uuid, current == null ? null : current.clone());
        }

        applyHelmet(player);
        startTitleTask(player);
    }

    public void removeFrozenExtras(Player player) {
        UUID uuid = player.getUniqueId();
        if (!frozen.contains(uuid)) return;

        frozen.remove(uuid);

        stopTitle(player);

        ItemStack cachedHelmet = helmetCache.remove(uuid);
        player.getInventory().setHelmet(cachedHelmet);
    }

    // ====================================================
    // Helmet
    // Display an indicator that a player is being
    // issued a screenshare.
    // ====================================================
    private void applyHelmet(Player player) {
        ItemStack ice = new ItemStack(Material.ICE);
        ItemMeta meta = ice.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(HELMET_NAME);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        meta.setUnbreakable(true);
        ice.setItemMeta(meta);

        player.getInventory().setHelmet(ice);
    }

    private void removeHelmet(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null && helmet.hasItemMeta() &&
                HELMET_NAME.equals(helmet.getItemMeta().getDisplayName())) {
            player.getInventory().setHelmet(null);
        }
    }


    // ====================================================
    // Title Display
    // Provide the frozen player Instructions to ensure they
    // aren't clueless on how to proceed.
    // ====================================================

    private void startTitleTask(Player player) {
        UUID uuid = player.getUniqueId();
        if (titleTasks.containsKey(uuid)) return;

        BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(
                plugin,
                () -> {
                    if (!player.isOnline() || !frozen.contains(uuid)) return;
                    player.sendTitle(
                            CC.t("&cFROZEN"),
                            CC.t("&aYou have been frozen join &b/discord, &a5 minutes"),
                            0, 40, 0
                    );
                },
                0L,
                60L
        );

        titleTasks.put(uuid, task);
    }


    private void stopTitle(Player player) {
        UUID uuid = player.getUniqueId();
        BukkitTask task = titleTasks.remove(uuid);
        if (task != null) task.cancel();
        player.resetTitle();
    }

}
