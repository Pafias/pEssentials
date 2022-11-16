package me.pafias.pafiasessentials.services;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.events.PlayerUnvanishedEvent;
import me.pafias.pafiasessentials.events.PlayerVanishedEvent;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager {

    private final PafiasEssentials plugin;

    public VanishManager(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    private final Set<UUID> vanished = new HashSet<>();

    public Set<UUID> getVanishedPlayers() {
        return vanished;
    }

    public boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public void vanish(Player player) {
        plugin.getServer().getOnlinePlayers().forEach(p -> {
            if (p != player && !p.hasPermission("essentials.vanish.bypass")) {
                p.hidePlayer(player);
            }
        });
        player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
        plugin.getServer().getPluginManager().callEvent(new PlayerVanishedEvent(player));
        vanished.add(player.getUniqueId());
        player.sendMessage(CC.t("&6Vanish: &aON"));
    }

    public void unvanish(Player player) {
        plugin.getServer().getOnlinePlayers().forEach(p -> {
            p.showPlayer(player);
        });
        player.setMetadata("vanished", new FixedMetadataValue(plugin, false));
        plugin.getServer().getPluginManager().callEvent(new PlayerUnvanishedEvent(player));
        vanished.remove(player.getUniqueId());
        player.sendMessage(CC.t("&6Vanish: &cOFF"));
    }

}
