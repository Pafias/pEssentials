package me.pafias.pafiasessentials.client.labymod.objects;

import java.util.UUID;

public class Addon {

    private UUID uuid;
    private String name;

    public Addon(String uuid, String name) {
        this.uuid = UUID.fromString(uuid);
        this.name = name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

}
