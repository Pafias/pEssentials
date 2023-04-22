package me.pafias.pessentials;

import me.pafias.pessentials.listeners.*;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.services.ServicesManager;
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
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        servicesManager = new ServicesManager(plugin);
        getServer().getOnlinePlayers().forEach(p -> servicesManager.getUserManager().addUser(p));
        register();
    }

    private void register() {
        PluginManager pm = getServer().getPluginManager();

        if (pm.isPluginEnabled("PlaceholderAPI"))
            servicesManager.getPAPIExpansion().register();

        pm.registerEvents(new JoinQuitListener(plugin), plugin);
        if (pm.isPluginEnabled("ProtocolLib")) {
            pm.registerEvents(new FlyListener(plugin), plugin);
            pm.registerEvents(new MoveListener(plugin), plugin);
            pm.registerEvents(new VanishListener(plugin), plugin);
        }
        pm.registerEvents(new TeleportListener(plugin), plugin);
        pm.registerEvents(new ChatListener(plugin), plugin);
        pm.registerEvents(new KnockbackListener(), plugin);
        pm.registerEvents(new PingListener(plugin), plugin);
        pm.registerEvents(new SitListener(), plugin);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(plugin);
        getServer().getOnlinePlayers().stream()
                .filter(p -> servicesManager.getVanishManager().isVanished(p))
                .forEach(p -> servicesManager.getVanishManager().unvanish(p));
        servicesManager.getUserManager().getUsers().stream().filter(User::hasIdentity).forEach(user -> user.setIdentity(user.getOriginalGameProfile()));
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            servicesManager.getPAPIExpansion().unregister();
        plugin = null;
    }

    public double parseVersion() {
        String version = getServer().getBukkitVersion();
        String[] var = version.split("\\.", 2);
        String[] var2 = var[1].split("-");
        String var3 = var2[0];
        return Double.parseDouble(var3);
    }

}
