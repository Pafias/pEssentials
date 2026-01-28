package me.pafias.pessentials.services;

import lombok.Getter;
import me.pafias.pessentials.events.PlayerUnvanishedEvent;
import me.pafias.pessentials.events.PlayerVanishedEvent;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager implements Listener {

    private final pEssentials plugin;

    public VanishManager(pEssentials plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Getter
    private final Set<UUID> vanishedPlayers = new HashSet<>();

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }

    public void vanish(Player player) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p != player && !p.hasPermission("essentials.vanish.bypass")) {
                p.hidePlayer(player);
            }
        }
        player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
        vanishedPlayers.add(player.getUniqueId());
        player.sendMessage(CC.t("&6Vanish: &aON"));
        plugin.getServer().getPluginManager().callEvent(new PlayerVanishedEvent(player));
    }

    public void unvanish(Player player) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.showPlayer(player);
        }
        player.setMetadata("vanished", new FixedMetadataValue(plugin, false));
        vanishedPlayers.remove(player.getUniqueId());
        player.sendMessage(CC.t("&6Vanish: &cOFF"));
        plugin.getServer().getPluginManager().callEvent(new PlayerUnvanishedEvent(player));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (vanishedPlayers.isEmpty()) return;
        for (final UUID uuid : vanishedPlayers) {
            if (!uuid.equals(event.getPlayer().getUniqueId())) {
                final Player player = plugin.getServer().getPlayer(uuid);
                if (player != null) {
                    if (!event.getPlayer().hasPermission("essentials.vanish.bypass"))
                        event.getPlayer().hidePlayer(player);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (isVanished(event.getPlayer()))
            unvanish(event.getPlayer());
    }

    public void shutdown() {
        for (UUID vanished : vanishedPlayers) {
            try {
                unvanish(Bukkit.getPlayer(vanished));
            } catch (Exception ignored) {
            }
        }
    }
}
