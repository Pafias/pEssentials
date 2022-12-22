package me.pafias.pafiasessentials.services;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.commands.CommandManager;
import me.pafias.pafiasessentials.objects.Variables;
import me.pafias.pafiasessentials.util.PAPIExpansion;

public class ServicesManager {

    public ServicesManager(PafiasEssentials plugin) {
        variables = new Variables(plugin);
        commandManager = new CommandManager(plugin);
        userManager = new UserManager();
        vanishManager = new VanishManager(plugin);
        freezeManager = new FreezeManager(plugin);
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            papiExpansion = new PAPIExpansion(plugin);
    }

    private PAPIExpansion papiExpansion;

    public PAPIExpansion getPAPIExpansion() {
        return papiExpansion;
    }

    private final UserManager userManager;

    public UserManager getUserManager() {
        return userManager;
    }

    private final Variables variables;

    public Variables getVariables() {
        return variables;
    }

    private final VanishManager vanishManager;

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    private final FreezeManager freezeManager;

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }

    private final CommandManager commandManager;

    public CommandManager getCommandManager() {
        return commandManager;
    }

}
