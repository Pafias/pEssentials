package me.pafias.pessentials.services;

import me.pafias.pessentials.objects.User;
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

import java.util.*;

public class FreezeManager implements Listener {

    private final pEssentials plugin;
    private final Set<UUID> frozen = new HashSet<>();

    private final Map<UUID, ItemStack> helmetCache = new HashMap<>();

    public FreezeManager(pEssentials plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // ====================================================
    // Listeners
    // ====================================================

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
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        User user = plugin.getSM().getUserManager().getUser(player);
        if (user != null) {
            user.destroy();
        }

        if (frozen.contains(uuid)) {
            plugin.getServer().broadcastMessage(CC.tf("\n&d&l%s &c&llogged out while frozen.", player.getName()) + "\n");
            helmetCache.remove(uuid);
        }
    }

    // ====================================================
    // Utility
    // ====================================================

    public void applyFrozen(Player player) {
        frozen.add(player.getUniqueId());
        applyHelmet(player);
    }

    public void removeFrozen(Player player) {
        frozen.remove(player.getUniqueId());
        removeHelmet(player);
    }

    private void applyHelmet(Player player) {
        helmetCache.put(player.getUniqueId(), player.getInventory().getHelmet());

        ItemStack ice = new ItemStack(Material.ICE);
        ItemMeta meta = ice.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(CC.t("&cFrozen"));
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        meta.setUnbreakable(true);
        ice.setItemMeta(meta);

        player.getInventory().setHelmet(ice);
    }

    private void removeHelmet(Player player) {
        ItemStack current = player.getInventory().getHelmet();

        if (current != null && current.hasItemMeta() && CC.t("&cFrozen").equals(current.getItemMeta().getDisplayName())) {
            ItemStack original = helmetCache.remove(player.getUniqueId());
            player.getInventory().setHelmet(original);
        }
    }

    public Set<UUID> getFrozenUsers() {
        return frozen;
    }
}