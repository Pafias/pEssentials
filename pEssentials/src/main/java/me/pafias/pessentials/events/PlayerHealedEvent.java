package me.pafias.pessentials.events;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerHealedEvent extends PlayerEvent {

    private final HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * -- GETTER --
     * Get the player or CommandSender who executed the heal command
     *
     * @return the player or CommandSender who executed the heal command
     */
    @Getter
    private final CommandSender healer;

    /**
     * -- GETTER --
     * Get the player who got healed
     *
     * @return the player who got healed
     */
    @Getter
    private final Player healed;

    /**
     * -- GETTER --
     * Get the health amount that the player had before he got healed
     * **
     *
     * @return double the health the player had before he got healed
     */
    @Getter
    private final double oldHealth;

    /***
     * This event is called after a player has been healed with this plugin's heal command
     * @param healer the player or CommandSender who executed the heal command
     * @param healed the player who got healed
     * @param oldHealth the health the player had before he got healed
     */
    public PlayerHealedEvent(CommandSender healer, Player healed, double oldHealth) {
        super(healed);
        this.healer = healer;
        this.healed = healed;
        this.oldHealth = oldHealth;
    }

    /***
     Get the difference between the new health (20) and the health before the player got healed
     @return double the difference between the new health (20) and the old health
     ***/
    public double getHealthDifference() {
        return healed.getHealth() - oldHealth;
    }

}
