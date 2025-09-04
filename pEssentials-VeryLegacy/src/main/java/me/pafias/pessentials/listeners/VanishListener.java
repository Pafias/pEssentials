package me.pafias.pessentials.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.VanishManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishListener extends PacketAdapter {

    public VanishListener(pEssentials plugin, VanishManager vanishManager) {
        super(plugin, PacketType.Status.Server.OUT_SERVER_INFO);
        this.vanishManager = vanishManager;
    }

    private final VanishManager vanishManager;

    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final WrappedServerPing ping = packet.getServerPings().read(0);
        final List<WrappedGameProfile> players = new ArrayList<>(ping.getPlayers());
        for (WrappedGameProfile profile : players) {
            if (profile == null || profile.getId() == null) continue;
            for (Player vanished : vanishManager.getVanishedPlayers())
                if (vanished.getUniqueId().toString().equals(profile.getId()))
                    players.remove(profile);
        }
        ping.setPlayers(players);
        packet.getServerPings().write(0, ping);
        event.setPacket(packet);
    }

}
