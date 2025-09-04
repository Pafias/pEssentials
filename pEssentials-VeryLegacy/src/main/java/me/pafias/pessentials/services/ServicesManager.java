package me.pafias.pessentials.services;

import lombok.Getter;
import me.pafias.pessentials.commands.CommandManager;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.protocol.PacketManager;
import me.pafias.pessentials.util.PAPIExpansion;

@Getter
public class ServicesManager {

    public ServicesManager(pEssentials plugin) {
        commandManager = new CommandManager(plugin);
        userManager = new UserManager();
        vanishManager = new VanishManager(plugin);
        freezeManager = new FreezeManager(plugin);
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            papiExpansion = new PAPIExpansion(userManager);
        if (plugin.getServer().getPluginManager().isPluginEnabled("packetevents"))
            packetManager = new PacketManager(plugin, vanishManager, userManager);
    }

    private PAPIExpansion papiExpansion;
    private final UserManager userManager;
    private final VanishManager vanishManager;
    private final FreezeManager freezeManager;
    private final CommandManager commandManager;
    private PacketManager packetManager;

}
