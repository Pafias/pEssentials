package me.pafias.pessentials;

import me.pafias.pessentials.listeners.*;
import me.pafias.pessentials.services.ServicesManager;
import me.pafias.pessentials.tasks.AutoUpdaterTask;
import me.pafias.putils.pUtils;
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
    public void onLoad() {
        plugin = this;
        pUtils.setPlugin(plugin);
    }

    @Override
    public void onEnable() {
        plugin.saveDefaultConfig();

        try {
            new AutoUpdaterTask(plugin).run();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

        servicesManager = new ServicesManager(plugin);
        servicesManager.onEnable();

        register();

        getServer().getOnlinePlayers().forEach(p -> servicesManager.getUserManager().addUser(p));
    }

    private void register() {
        PluginManager pm = getServer().getPluginManager();

        if (pm.isPluginEnabled("PlaceholderAPI"))
            servicesManager.getPapiExpansion().register();

        pm.registerEvents(new JoinQuitListener(plugin), plugin);
        if (pm.isPluginEnabled("packetevents")) {
            pm.registerEvents(new FlyListener(plugin), plugin);
            pm.registerEvents(new MoveListener(plugin), plugin);
            pm.registerEvents(new VanishListener(plugin), plugin);
        }
        pm.registerEvents(new TeleportListener(plugin), plugin);
        pm.registerEvents(new ChatListener(plugin), plugin);
        pm.registerEvents(new PingListener(plugin), plugin);
        pm.registerEvents(new SitListener(), plugin);
        pm.registerEvents(new BadPeopleListener(plugin), plugin);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(plugin);
        servicesManager.onDisable();
    }

}
