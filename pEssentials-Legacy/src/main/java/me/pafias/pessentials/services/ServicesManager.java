package me.pafias.pessentials.services;

import lombok.Getter;
import me.pafias.pessentials.commands.CommandManager;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.PAPIExpansion;

@Getter
public class ServicesManager {

    public ServicesManager(pEssentials plugin) {
        commandManager = new CommandManager(plugin);
        userManager = new UserManager();
        vanishManager = new VanishManager(plugin);
        freezeManager = new FreezeManager(plugin);
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            papiExpansion = new PAPIExpansion(plugin);
    }

    public void onDisable() {
        try {
            if (papiExpansion != null)
                papiExpansion.unregister();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            freezeManager.shutdown();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            vanishManager.shutdown();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            userManager.shutdown();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private PAPIExpansion papiExpansion;
    private final UserManager userManager;
    private final VanishManager vanishManager;
    private final FreezeManager freezeManager;
    private final CommandManager commandManager;

}
