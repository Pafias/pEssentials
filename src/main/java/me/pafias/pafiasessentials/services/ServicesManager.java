package me.pafias.pafiasessentials.services;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.nms.NMSProvider;
import me.pafias.pafiasessentials.nms.NMSVersionProvider;
import me.pafias.pafiasessentials.objects.Variables;
import me.pafias.pafiasessentials.util.PAPIExpansion;

public class ServicesManager {

    private final PafiasEssentials plugin;

    public ServicesManager(PafiasEssentials plugin) {
        this.plugin = plugin;
        variables = new Variables(plugin);
        nmsProvider = NMSVersionProvider.getProvider();
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

    private final NMSProvider nmsProvider;

    public NMSProvider getNMSProvider() {
        return nmsProvider;
    }

    private final FreezeManager freezeManager;

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }

}
