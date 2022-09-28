package me.pafias.pafiasessentials.client.labymod.objects;

public class Mod {

    private String hash;
    private String name;

    public Mod(String hash, String name) {
        this.hash = hash;
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public String getName() {
        return name;
    }

}
