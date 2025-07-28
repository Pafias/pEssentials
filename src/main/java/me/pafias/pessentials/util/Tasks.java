package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import org.bukkit.scheduler.BukkitTask;

public class Tasks {

    private static final pEssentials plugin = pEssentials.get();

    public static BukkitTask runSync(final Runnable runnable) {
        return plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public static BukkitTask runAsync(final Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static BukkitTask runLaterSync(final long delay, final Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public static BukkitTask runLaterAsync(final long delay, final Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public static BukkitTask runRepeatingSync(final long delay, final long period, final Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }

    public static BukkitTask runRepeatingAsync(final long delay, final long period, final Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
    }

}
