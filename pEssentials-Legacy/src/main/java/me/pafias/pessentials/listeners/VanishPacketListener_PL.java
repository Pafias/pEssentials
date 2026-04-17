package me.pafias.pessentials.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.VanishManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class VanishPacketListener_PL extends PacketAdapter implements VanishPacketListener {

    private final VanishManager vanishManager;

    public VanishPacketListener_PL(VanishManager vanishManager) {
        super(pEssentials.get(), com.comphenix.protocol.PacketType.Status.Server.SERVER_INFO);
        this.vanishManager = vanishManager;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (vanishManager.getVanishedPlayers().isEmpty()) return;
        try {
            final WrappedServerPing ping = event.getPacket().getServerPings().read(0);

            final List<WrappedGameProfile> players = new ArrayList<>(ping.getPlayers());
            if (players == null || players.isEmpty()) return;

            final Iterator<WrappedGameProfile> iterator = players.iterator();
            while (iterator.hasNext()) {
                final WrappedGameProfile profile = iterator.next();
                final UUID uuid = profile.getUUID();

                if (vanishManager.getVanishedPlayers().contains(uuid))
                    iterator.remove();
            }

            final int online = ping.getPlayersOnline();
            if (online >= 0)
                ping.setPlayersOnline(Math.max(0, online - vanishManager.getVanishedPlayers().size()));

            event.getPacket().getServerPings().write(0, ping);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this);
    }

}


