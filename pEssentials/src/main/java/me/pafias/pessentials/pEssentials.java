package me.pafias.pessentials;

import me.pafias.pessentials.listeners.*;
import me.pafias.pessentials.services.ServicesManager;
import me.pafias.pessentials.tasks.AutoUpdaterTask;
import me.pafias.putils.pUtils;
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
    public void onLoad() {
        plugin = this;
        pUtils.setPlugin(plugin);
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        try {
            new AutoUpdaterTask(plugin).run();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

        servicesManager = new ServicesManager(plugin);
        servicesManager.onEnable();

        register();

        for (Player online : getServer().getOnlinePlayers()) {
            if (!online.hasMetadata("NPC"))
                servicesManager.getUserManager().addUser(online);
        }
    }

    private void register() {
        PluginManager pm = getServer().getPluginManager();

        if (pm.isPluginEnabled("PlaceholderAPI"))
            servicesManager.getPapiExpansion().register();

        pm.registerEvents(new JoinQuitListener(plugin), plugin);
        if (pm.isPluginEnabled("packetevents")) {
            if (com.github.retrooper.packetevents.PacketEvents.getAPI().getServerManager()
                    .getVersion().isNewerThanOrEquals(com.github.retrooper.packetevents.manager.server.ServerVersion.V_1_21_2))
                pm.registerEvents(new FlyProtocolListener(plugin), plugin);
            else
                pm.registerEvents(new FlyProtocolListenerOld(plugin), plugin);
        }
        pm.registerEvents(new TeleportListener(servicesManager.getUserManager()), plugin);
        pm.registerEvents(new ChatListener(plugin, servicesManager.getUserManager()), plugin);
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
