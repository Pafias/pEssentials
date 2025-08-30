package me.pafias.pessentials;

import me.pafias.pessentials.listeners.*;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.services.ServicesManager;
import me.pafias.pessentials.tasks.AutoUpdaterTask;
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

        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();

        try {
            new AutoUpdaterTask(plugin).run();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

        servicesManager = new ServicesManager(plugin);

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
        getServer().getOnlinePlayers().stream()
                .filter(p -> servicesManager.getVanishManager().isVanished(p))
                .forEach(p -> servicesManager.getVanishManager().unvanish(p));
        servicesManager.getUserManager().getUsers().values().stream().filter(User::hasIdentity).forEach(user -> user.setIdentity(user.getOriginalGameProfile()));
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            servicesManager.getPapiExpansion().unregister();
    }

    public double parseVersion() {
        try {
            String version = getServer().getMinecraftVersion(); // 1.12.2
            String[] var = version.split("\\.", 2); // [1, 12.2]
            return Double.parseDouble(var[1]); // 12.2
        } catch (Throwable t) {
            String version = getServer().getBukkitVersion(); // 1.12.2-R0.1-SNAPSHOT
            String[] var = version.split("\\.", 2); // [1, 12.2-R0.1-SNAPSHOT]
            String[] var2 = var[1].split("-"); // [12.2, R0.1, SNAPSHOT]
            return Double.parseDouble(var2[0]); // 12.2
        }
    }

}
