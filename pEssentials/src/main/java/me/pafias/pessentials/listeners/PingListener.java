package me.pafias.pessentials.listeners;

import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.putils.Tasks;
import org.bukkit.event.Listener;

public class PingListener implements Listener {

    public PingListener(pEssentials plugin) {
        if (plugin.getConfig().getBoolean("kick_on_high_ping")) {
            final int threshold = plugin.getConfig().getInt("ping_kick_threshold");
            Tasks.runRepeatingAsync(50, 400, () -> {
                plugin.getServer().getOnlinePlayers().stream()
                        .filter(p -> !p.hasPermission("essentials.ping.bypass"))
                        .forEach(p -> {
                            int ping = p.getPing();
                            if (ping > threshold)
                                Tasks.runSync(() -> {
                                    p.kick(CC.af("&cYour ping is too high. (%dms)", ping));
                                });
                        });
            });
        }
    }

}
