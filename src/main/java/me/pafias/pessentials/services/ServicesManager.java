package me.pafias.pessentials.services;

import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.commands.CommandManager;
import me.pafias.pessentials.objects.Variables;
import me.pafias.pessentials.util.PAPIExpansion;

public class ServicesManager {

    public ServicesManager(pEssentials plugin) {
        variables = new Variables(plugin);
        commandManager = new CommandManager(plugin, variables);
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
