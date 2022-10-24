package me.pafias.pafiasessentials.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.common.collect.ImmutableList;
import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.stream.Collectors;

public class VanishListener implements Listener {

    private final PafiasEssentials pe;

    public VanishListener(PafiasEssentials plugin) {
        this.pe = plugin;
        if (plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
            ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(pe, ListenerPriority.HIGH, PacketType.Status.Server.SERVER_INFO) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    PacketContainer packet = event.getPacket();
                    WrappedServerPing ping = packet.getServerPings().read(0);
                    ImmutableList<WrappedGameProfile> players = ping.getPlayers();
                    ping.setPlayers(players.stream().filter(p -> !pe.getSM().getVanishManager().getVanishedPlayers().contains(p.getUUID())).collect(Collectors.toSet()));
                    packet.getServerPings().write(0, ping);
                    event.setPacket(packet);
                }
            });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        pe.getSM().getVanishManager().getVanishedPlayers().stream()
                .filter(uuid -> !uuid.equals(event.getPlayer().getUniqueId()))
                .filter(uuid -> pe.getServer().getPlayer(uuid) != null)
                .forEach(uuid -> {
                    if (!event.getPlayer().hasPermission("essentials.vanish.bypass")) {
                        event.getPlayer().hidePlayer(pe.getServer().getPlayer(uuid));
                    }
                });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (pe.getSM().getVanishManager().isVanished(event.getPlayer()))
            pe.getSM().getVanishManager().unvanish(event.getPlayer());
    }

}
