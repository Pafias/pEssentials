package me.pafias.pessentials.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketStatusSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.pafias.pessentials.services.VanishManager;

import java.util.Iterator;
import java.util.UUID;

public class VanishPacketListener extends SimplePacketListenerAbstract {

    private final VanishManager vanishManager;

    public VanishPacketListener(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketStatusSend(PacketStatusSendEvent event) {
        if (event.getPacketType() == PacketType.Status.Server.RESPONSE) {
            if (vanishManager.getVanishedPlayers().isEmpty()) return;
            try {
                final WrapperStatusServerResponse packet = new WrapperStatusServerResponse(event);
                final JsonObject json = packet.getComponent();

                final JsonObject playersJson = json.getAsJsonObject("players");
                if (playersJson == null) return;

                final JsonArray sampleJson = playersJson.getAsJsonArray("sample");
                if (sampleJson == null) return;

                final Iterator<JsonElement> iterator = sampleJson.iterator();
                while (iterator.hasNext()) {
                    final JsonElement element = iterator.next();
                    if (element.isJsonObject()) {
                        final JsonObject playerJson = element.getAsJsonObject();
                        final UUID uuid = UUID.fromString(playerJson.get("id").getAsString());

                        if (vanishManager.getVanishedPlayers().contains(uuid))
                            iterator.remove();
                    }
                }

                final JsonPrimitive online = playersJson.getAsJsonPrimitive("online");
                if (online != null)
                    playersJson.addProperty("online", Math.max(0, online.getAsInt() - vanishManager.getVanishedPlayers().size()));

                packet.setComponent(json);
                packet.write();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void shutdown() {
        PacketEvents.getAPI().getEventManager().unregisterListener(this);
    }

}
