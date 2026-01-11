package me.pafias.pessentials.services;

import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.putils.builders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FreezeManager implements Listener {

    private final pEssentials plugin;
    private final Set<UUID> frozen = new HashSet<>();

    private final Map<UUID, ItemStack> helmetCache = new HashMap<>();

    public FreezeManager(pEssentials plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
        final User user = plugin.getSM().getUserManager().getUser(event.getPlayer());
        if (user == null) return;

        user.destroy();
        if (frozen.contains(user.getUUID())) {
            plugin.getServer().broadcastMessage(CC.tf("&d&l%s &c&llogged out while frozen.", user.getName()));
            helmetCache.remove(user.getUUID());
        }
    }

    public void applyFrozen(Player player) {
        frozen.add(player.getUniqueId());
        applyHelmet(player);
    }

    public void removeFrozen(Player player) {
        frozen.remove(player.getUniqueId());
        removeHelmet(player);
    }

    private static final ItemStack ICE = new ItemBuilder(Material.ICE)
            .setName(CC.a("&cFrozen"))
            .addEnchant(Enchantment.BINDING_CURSE, 1)
            .build();

    private void applyHelmet(Player player) {
        helmetCache.put(player.getUniqueId(), player.getInventory().getHelmet());
        player.getInventory().setHelmet(ICE);
    }

    private void removeHelmet(Player player) {
        final ItemStack current = player.getInventory().getHelmet();

        if (current != null && current.equals(ICE)) {
            final ItemStack cached = helmetCache.remove(player.getUniqueId());
            if (cached != null)
                player.getInventory().setHelmet(cached);
        }
    }

    public Set<UUID> getFrozenUsers() {
        return frozen;
    }
}