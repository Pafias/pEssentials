package me.pafias.pessentials.services;

import me.pafias.pessentials.events.PlayerUnvanishedEvent;
import me.pafias.pessentials.events.PlayerVanishedEvent;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
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

    private final Set<UUID> vanished = new HashSet<>();

    public Set<UUID> getVanishedPlayers() {
        return vanished;
    }

    public boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public void vanish(Player player) {
        plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> p != player && !p.hasPermission("essentials.vanish.bypass"))
                .forEach(p -> p.hidePlayer(player));
        player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
        plugin.getServer().getPluginManager().callEvent(new PlayerVanishedEvent(player));
        vanished.add(player.getUniqueId());
        player.sendMessage(CC.t("&6Vanish: &aON"));
    }

    public void unvanish(Player player) {
        plugin.getServer().getOnlinePlayers()
                .forEach(p -> p.showPlayer(player));
        player.setMetadata("vanished", new FixedMetadataValue(plugin, false));
        plugin.getServer().getPluginManager().callEvent(new PlayerUnvanishedEvent(player));
        vanished.remove(player.getUniqueId());
        player.sendMessage(CC.t("&6Vanish: &cOFF"));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (vanished.isEmpty()) return;
        vanished.stream()
                .filter(uuid -> !uuid.equals(event.getPlayer().getUniqueId()))
                .filter(uuid -> plugin.getServer().getPlayer(uuid) != null)
                .forEach(uuid -> {
                    if (!event.getPlayer().hasPermission("essentials.vanish.bypass"))
                        event.getPlayer().hidePlayer(plugin.getServer().getPlayer(uuid));
                });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (isVanished(event.getPlayer()))
            unvanish(event.getPlayer());
    }

}
