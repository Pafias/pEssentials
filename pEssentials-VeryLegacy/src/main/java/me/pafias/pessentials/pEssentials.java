package me.pafias.pessentials;

import me.pafias.pessentials.listeners.*;
import me.pafias.pessentials.services.ServicesManager;
import me.pafias.pessentials.tasks.AutoUpdaterTask;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class pEssentials extends JavaPlugin {

    private static pEssentials plugin;

    public static pEssentials get() {
        return plugin;
    }

    private ServicesManager servicesManager;

    public ServicesManager getSM() {
        return servicesManager;
    }

    @Override
    public void onEnable() {
        plugin = this;

        plugin.saveDefaultConfig();

        try {
            new AutoUpdaterTask(plugin).run();
        } catch (Throwable t) {
            getLogger().warning("Failed to check for updates.");
        }

        getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

        servicesManager = new ServicesManager(plugin);

        register();

        for (Player p : getServer().getOnlinePlayers())
            servicesManager.getUserManager().addUser(p);
    }

    private void register() {
        PluginManager pm = getServer().getPluginManager();

        if (pm.isPluginEnabled("PlaceholderAPI"))
            servicesManager.getPapiExpansion().register();

        pm.registerEvents(new JoinQuitListener(plugin), plugin);
        pm.registerEvents(new TeleportListener(plugin), plugin);
        pm.registerEvents(new ChatListener(plugin), plugin);
        pm.registerEvents(new PingListener(plugin), plugin);
        pm.registerEvents(new SitListener(), plugin);
        pm.registerEvents(new BadPeopleListener(plugin), plugin);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(plugin);
        try {
            servicesManager.getPacketManager().shutdown();
        } catch (Throwable ignored) {
        }
        for (Player p : getServer().getOnlinePlayers()) {
            try {
                if (servicesManager.getVanishManager().isVanished(p))
                    servicesManager.getVanishManager().unvanish(p);
            } catch (Throwable ignored) {
            }
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            servicesManager.getPapiExpansion().unregister();
    }

    public double parseVersion() {
        String version = getServer().getBukkitVersion(); // 1.6.4-R2.1-SNAPSHOT
        String[] var = version.split("\\.", 2); // [1, 6.4-R2.1-SNAPSHOT]
        String[] var2 = var[1].split("-"); // [6.4, R2.1, SNAPSHOT]
        return Double.parseDouble(var2[0]); // 6.4
    }

}
