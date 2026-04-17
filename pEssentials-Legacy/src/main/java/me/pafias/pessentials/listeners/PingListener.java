package me.pafias.pessentials.listeners;

import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.Reflection;
import me.pafias.putils.LCC;
import me.pafias.putils.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PingListener implements Listener {

    public PingListener(pEssentials plugin) {
        if (plugin.getConfig().getBoolean("kick_on_high_ping")) {
            final int threshold = plugin.getConfig().getInt("ping_kick_threshold");
            Tasks.runRepeatingAsync(50, 400, () -> {
                for (final Player p : plugin.getServer().getOnlinePlayers()) {
                    if (!p.hasPermission("essentials.ping.bypass")) {
                        int ping = Reflection.getPing(p);
                        if (ping > threshold)
                            Tasks.runSync(() -> {
                                p.kickPlayer(LCC.tf("&cYour ping is too high. (%dms)", ping));
                            });
                    }
                }
            });
        }
    }

}
