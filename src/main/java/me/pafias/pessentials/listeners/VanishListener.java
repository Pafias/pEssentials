package me.pafias.pessentials.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.common.collect.ImmutableList;
import me.pafias.pessentials.pEssentials;
import org.bukkit.event.Listener;

import java.util.stream.Collectors;

public class VanishListener implements Listener {

    private final pEssentials pe;

    public VanishListener(pEssentials plugin) {
        this.pe = plugin;
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(pe, ListenerPriority.HIGH, PacketType.Status.Server.SERVER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final WrappedServerPing ping = packet.getServerPings().read(0);
                final ImmutableList<WrappedGameProfile> players = ping.getPlayers();
                ping.setPlayers(players
                        .stream()
                        .filter(p -> !pe.getSM().getVanishManager().getVanishedPlayers().contains(p.getUUID()))
                        .collect(Collectors.toSet())
                );
                packet.getServerPings().write(0, ping);
                event.setPacket(packet);
            }
        });
    }

}
