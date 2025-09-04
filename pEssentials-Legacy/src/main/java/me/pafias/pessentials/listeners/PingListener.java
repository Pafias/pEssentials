package me.pafias.pessentials.listeners;

import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Reflection;
import me.pafias.pessentials.util.Tasks;
import org.bukkit.event.Listener;

public class PingListener implements Listener {

    public PingListener(pEssentials plugin) {
        if (plugin.getConfig().getBoolean("kick_on_high_ping"))
            Tasks.runRepeatingAsync(50, 400, () -> {
                plugin.getServer().getOnlinePlayers().stream().filter(p -> !p.hasPermission("essentials.ping.bypass")).forEach(p -> {
                    int ping = Reflection.getPing(p);
                    if (ping > plugin.getConfig().getInt("ping_kick_threshold"))
                        Tasks.runSync(() -> {
                            p.kickPlayer(CC.tf("&cYour ping is too high. (%dms)", ping));
                        });
                });
            });
    }

}
