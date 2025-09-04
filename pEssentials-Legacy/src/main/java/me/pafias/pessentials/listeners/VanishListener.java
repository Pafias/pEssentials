package me.pafias.pessentials.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketStatusSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.pafias.pessentials.pEssentials;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.UUID;

public class VanishListener implements Listener {

    private final pEssentials pe;

    public VanishListener(pEssentials plugin) {
        this.pe = plugin;
        PacketEvents.getAPI().getEventManager().registerListener(new SimplePacketListenerAbstract() {
            @Override
            public void onPacketStatusSend(@NotNull PacketStatusSendEvent event) {
                if (!event.getPacketType().equals(PacketType.Status.Server.RESPONSE)) return;
                try {
                    final WrapperStatusServerResponse packet = new WrapperStatusServerResponse(event);
                    final JsonObject json = packet.getComponent();
                    JsonObject playersJson = json.getAsJsonObject("players");
                    if (playersJson == null) return;
                    JsonArray sampleJson = playersJson.getAsJsonArray("sample");
                    if (sampleJson == null) return;
                    Iterator<JsonElement> iterator = sampleJson.iterator();
                    while (iterator.hasNext()) {
                        JsonElement element = iterator.next();
                        if (element.isJsonObject()) {
                            JsonObject playerJson = element.getAsJsonObject();
                            UUID uuid = UUID.fromString(playerJson.get("id").getAsString());
                            if (pe.getSM().getVanishManager().getVanishedPlayers().contains(uuid))
                                iterator.remove();
                        }
                    }
                    packet.setComponent(json);
                    packet.write();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
