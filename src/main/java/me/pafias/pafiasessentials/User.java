package me.pafias.pafiasessentials;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User {

    private final Player player;

    public boolean flyingEntity;

    public boolean movingEntity;

    public Location lastLocation;

    private boolean inStaffchat;

    public User(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    public String getName() {
        return this.player.getName();
    }

    public boolean isInStaffChat() {
        return inStaffchat;
    }

    public void setInStaffchat(boolean inStaffChat) {
        this.inStaffchat = inStaffChat;
    }

}
