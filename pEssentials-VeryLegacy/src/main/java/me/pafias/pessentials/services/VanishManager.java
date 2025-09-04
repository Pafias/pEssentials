package me.pafias.pessentials.services;

import lombok.Getter;
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

public class VanishManager implements Listener {

    private final pEssentials plugin;

    public VanishManager(pEssentials plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Getter
    private final Set<Player> vanishedPlayers = new HashSet<>();

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }

    public void vanish(Player player) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p != player && !p.hasPermission("essentials.vanish.bypass"))
                p.hidePlayer(player);
        }
        player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
        vanishedPlayers.add(player);
        player.sendMessage(CC.t("&6Vanish: &aON"));
        plugin.getServer().getPluginManager().callEvent(new PlayerVanishedEvent(player));
    }

    public void unvanish(Player player) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.showPlayer(player);
        }
        player.setMetadata("vanished", new FixedMetadataValue(plugin, false));
        vanishedPlayers.remove(player);
        player.sendMessage(CC.t("&6Vanish: &cOFF"));
        plugin.getServer().getPluginManager().callEvent(new PlayerUnvanishedEvent(player));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (vanishedPlayers.isEmpty()) return;
        for (Player player : vanishedPlayers) {
            if (player != null && !player.equals(event.getPlayer())) {
                if (!event.getPlayer().hasPermission("essentials.vanish.bypass"))
                    event.getPlayer().hidePlayer(player);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (isVanished(event.getPlayer()))
            unvanish(event.getPlayer());
    }

}
