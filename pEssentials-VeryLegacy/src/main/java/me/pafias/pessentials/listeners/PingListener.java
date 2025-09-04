package me.pafias.pessentials.listeners;

import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Reflection;
import me.pafias.pessentials.util.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PingListener implements Listener {

    public PingListener(final pEssentials plugin) {
        if (plugin.getConfig().getBoolean("kick_on_high_ping"))
            Tasks.runRepeatingAsync(50, 400, new BukkitRunnable() {
                @Override
                public void run() {
                    for (final Player p : plugin.getServer().getOnlinePlayers()) {
                        if (!p.hasPermission("essentials.ping.bypass")) {
                            final int ping = Reflection.getPing(p);
                            if (ping > plugin.getConfig().getInt("ping_kick_threshold"))
                                Tasks.runSync(new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        p.kickPlayer(CC.tf("&cYour ping is too high. (%dms)", ping));
                                    }
                                });
                        }
                    }
                }
            });
    }

}
