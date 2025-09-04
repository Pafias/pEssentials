package me.pafias.pessentials.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerVanishedEvent extends PlayerEvent {

    private final HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    /***
     * This event is called when a player executes the vanish command to vanish
     * @param player the player who vanished
     */
    public PlayerVanishedEvent(Player player) {
        super(player);
    }

}
