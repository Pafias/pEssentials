package me.pafias.pessentials.services;

import lombok.Getter;
import me.pafias.pessentials.events.PlayerUnvanishedEvent;
import me.pafias.pessentials.events.PlayerVanishedEvent;
import me.pafias.pessentials.listeners.VanishPacketListener;
import me.pafias.pessentials.listeners.VanishPacketListener_PE;
import me.pafias.pessentials.listeners.VanishPacketListener_PL;
import me.pafias.pessentials.pEssentials;
import me.pafias.putils.LCC;
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

    private final VanishPacketListener vanishPacketListener;

    private static final FixedMetadataValue VANISHED_META = new FixedMetadataValue(pEssentials.get(), true);
    private static final FixedMetadataValue UNVANISHED_META = new FixedMetadataValue(pEssentials.get(), false);


    public VanishManager(pEssentials plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (plugin.getServer().getPluginManager().isPluginEnabled("packetevents"))
            vanishPacketListener = new VanishPacketListener_PE(this);
        else if (plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
            vanishPacketListener = new VanishPacketListener_PL(this);
        else vanishPacketListener = null;
    }

    @Getter
    private final Set<UUID> vanishedPlayers = new HashSet<>(3);

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }

    public void vanish(Player player) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p != player && !p.hasPermission("essentials.vanish.bypass")) {
                p.hidePlayer(player);
            }
        }
        player.setMetadata("vanished", VANISHED_META);
        vanishedPlayers.add(player.getUniqueId());
        player.sendMessage(LCC.t("&6Vanish: &aON"));
        plugin.getServer().getPluginManager().callEvent(new PlayerVanishedEvent(player));
    }

    public void unvanish(Player player) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.showPlayer(player);
        }
        player.setMetadata("vanished", UNVANISHED_META);
        vanishedPlayers.remove(player.getUniqueId());
        player.sendMessage(LCC.t("&6Vanish: &cOFF"));
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
            final Player player = Bukkit.getPlayer(vanished);
            if (player != null)
                unvanish(player);
        }
        if (vanishPacketListener != null)
            vanishPacketListener.shutdown();
    }

}
