package me.pafias.pessentials.listeners;

import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Reflection;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PingListener implements Listener {

    public PingListener(pEssentials plugin) {
        if (plugin.getSM().getVariables().highPingKick)
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getServer().getOnlinePlayers().stream().filter(p -> !p.hasPermission("essentials.ping.bypass")).forEach(p -> {
                        int ping = Reflection.getPing(p);
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
