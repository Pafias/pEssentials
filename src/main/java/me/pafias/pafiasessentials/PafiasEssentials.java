package me.pafias.pafiasessentials;

import me.pafias.pafiasessentials.antiexploit.Log4j;
import me.pafias.pafiasessentials.christmas.ChristmasTreeCommand;
import me.pafias.pafiasessentials.commands.*;
import me.pafias.pafiasessentials.commands.gamemodes.GMACommand;
import me.pafias.pafiasessentials.commands.gamemodes.GMCCommand;
import me.pafias.pafiasessentials.commands.gamemodes.GMSCommand;
import me.pafias.pafiasessentials.commands.gamemodes.GMSPCommand;
import me.pafias.pafiasessentials.commands.messaging.ReplyCommand;
import me.pafias.pafiasessentials.commands.messaging.TellCommand;
import me.pafias.pafiasessentials.commands.teleport.TeleportCommand;
import me.pafias.pafiasessentials.commands.teleport.TphereCommand;
import me.pafias.pafiasessentials.commands.weather.DayCommand;
import me.pafias.pafiasessentials.commands.weather.NightCommand;
import me.pafias.pafiasessentials.commands.weather.RainCommand;
import me.pafias.pafiasessentials.commands.weather.SunCommand;
import me.pafias.pafiasessentials.listeners.*;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.services.ServicesManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PafiasEssentials extends JavaPlugin {

    private static PafiasEssentials plugin;

    public static PafiasEssentials get() {
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
        pm.registerEvents(new Log4j(plugin), plugin);
        pm.registerEvents(new KnockbackListener(), plugin);

        getCommand("entity").setExecutor(new EntityCommand(plugin));
        getCommand("sound").setExecutor(new SoundCommand(plugin));
        getCommand("nightvision").setExecutor(new NightvisionCommand(plugin));
        getCommand("ping").setExecutor(new PingCommand(plugin));
        getCommand("gamemode").setExecutor(new GamemodeCommand(plugin));
        getCommand("gmc").setExecutor(new GMCCommand(plugin));
        getCommand("gms").setExecutor(new GMSCommand(plugin));
        getCommand("gmsp").setExecutor(new GMSPCommand(plugin));
        getCommand("gma").setExecutor(new GMACommand(plugin));
        getCommand("hub").setExecutor(new HubCommand(plugin));
        getCommand("heal").setExecutor(new HealCommand(plugin));
        getCommand("speed").setExecutor(new SpeedCommand(plugin));
        getCommand("tell").setExecutor(new TellCommand(plugin));
        getCommand("reply").setExecutor(new ReplyCommand(plugin));
        getCommand("teleport").setExecutor(new TeleportCommand(plugin));
        getCommand("teleporthere").setExecutor(new TphereCommand(plugin));
        getCommand("skull").setExecutor(new SkullCommand(plugin));
        getCommand("back").setExecutor(new BackCommand(plugin));
        getCommand("top").setExecutor(new TopCommand());
        getCommand("day").setExecutor(new DayCommand());
        getCommand("night").setExecutor(new NightCommand());
        getCommand("lag").setExecutor(new LagCommand(plugin));
        getCommand("item").setExecutor(new ItemCommand());
        getCommand("staffchat").setExecutor(new StaffchatCommand(plugin));
        getCommand("vanish").setExecutor(new VanishCommand(plugin));
        getCommand("copyinventory").setExecutor(new CopyInventoryCommand(plugin));
        getCommand("crash").setExecutor(new CrashCommand(plugin));
        getCommand("christmastree").setExecutor(new ChristmasTreeCommand());
        getCommand("armorstand").setExecutor(new ArmorstandCommand());
        getCommand("identity").setExecutor(new IdentityCommand(plugin));
        getCommand("itemstack").setExecutor(new ItemstackCommand(plugin));
        getCommand("whois").setExecutor(new WhoisCommand(plugin));
        getCommand("sun").setExecutor(new SunCommand());
        getCommand("rain").setExecutor(new RainCommand());
        getCommand("knockback").setExecutor(new KnockbackCommand());
        getCommand("feed").setExecutor(new FeedCommand(plugin));
        getCommand("sudo").setExecutor(new SudoCommand(plugin));
        getCommand("russianroulette").setExecutor(new RussianrouletteCommand(plugin));
        getCommand("launch").setExecutor(new LaunchCommand(plugin));
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

}
