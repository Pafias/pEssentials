package me.pafias.pafiasessentials.listeners;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PingListener implements Listener {

    public PingListener(PafiasEssentials plugin) {
        if (plugin.getSM().getVariables().highPingKick)
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getServer().getOnlinePlayers().stream().filter(p -> !p.hasPermission("essentials.ping.bypass")).forEach(p -> {
                        int ping = plugin.getSM().getNMSProvider().getPing(p);
                        if (ping > plugin.getSM().getVariables().pingKickThreshold)
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.kickPlayer(CC.tf("&cYour ping is too high. (%dms)", ping));
                                }
                            }.runTask(plugin);
                    });
                }
            }.runTaskTimerAsynchronously(plugin, 50, 20 * 20);
    }

}
