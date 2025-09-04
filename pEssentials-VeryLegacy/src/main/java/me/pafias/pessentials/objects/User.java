package me.pafias.pessentials.objects;

import lombok.Getter;
import lombok.Setter;
import me.pafias.pessentials.pEssentials;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

    @Getter
    private final Player player;

    public boolean flyingEntity, movingEntity;
    public Location lastLocation;

    @Getter
    @Setter
    private boolean inStaffchat;

    @Setter
    @Getter
    private boolean blockingPMs;

    @Getter
    private final Set<String> blocking = new HashSet<>();

    public User(Player player) {
        this.player = player;
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    public String getName() {
        return player.getName();
    }

    public void crash() {
        for (int i = 0; i < 5; i++)
            player.playEffect(player.getLocation(), Effect.CRIT, Integer.MAX_VALUE);
    }

    public boolean isVanished() {
        return pEssentials.get().getSM().getVanishManager().isVanished(player);
    }

    public boolean isFrozen() {
        return pEssentials.get().getSM().getFreezeManager().getFrozenUsers().contains(player.getUniqueId());
    }

    public void setFrozen(boolean frozen) {
        if (frozen)
            pEssentials.get().getSM().getFreezeManager().getFrozenUsers().add(player.getUniqueId());
        else
            pEssentials.get().getSM().getFreezeManager().getFrozenUsers().remove(player.getUniqueId());
    }

}
