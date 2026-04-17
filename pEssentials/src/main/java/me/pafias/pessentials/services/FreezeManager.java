package me.pafias.pessentials.services;

import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.pEssentials;
import me.pafias.putils.CC;
import me.pafias.putils.builders.ModernItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) return;
        if (!frozen.contains(event.getPlayer().getUniqueId())) return;
        event.setTo(from);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        final User user = plugin.getSM().getUserManager().getUser(event.getPlayer());
        if (user == null) return;

        if (user.getFreezeTask() != null)
            user.getFreezeTask().cancel();
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

    private static final ItemStack ICE = new ModernItemBuilder(Material.ICE)
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

    public void shutdown() {
        for (final UUID uuid : frozen) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null)
                removeFrozen(player);
        }
    }
}