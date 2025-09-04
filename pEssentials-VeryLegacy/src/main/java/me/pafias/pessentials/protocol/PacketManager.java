package me.pafias.pessentials.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import me.pafias.pessentials.listeners.FlyListener;
import me.pafias.pessentials.listeners.MoveListener;
import me.pafias.pessentials.listeners.VanishListener;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.UserManager;
import me.pafias.pessentials.services.VanishManager;

import java.util.HashSet;
import java.util.Set;

public class PacketManager {

    public PacketManager(pEssentials plugin, VanishManager vanishManager, UserManager userManager) {
        listeners.add(new VanishListener(plugin, vanishManager));
        listeners.add(new FlyListener(plugin, userManager));
        listeners.add(new MoveListener(plugin, userManager));

        for (PacketAdapter listener : listeners)
            ProtocolLibrary.getProtocolManager().addPacketListener(listener);
    }

    private final Set<PacketAdapter> listeners = new HashSet<>();

    public void shutdown() {
        for (PacketAdapter listener : listeners)
            ProtocolLibrary.getProtocolManager().removePacketListener(listener);
    }

}
