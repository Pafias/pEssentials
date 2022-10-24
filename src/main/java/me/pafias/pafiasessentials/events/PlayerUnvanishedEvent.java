package me.pafias.pafiasessentials.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerUnvanishedEvent extends PlayerEvent {

    private final HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    /***
     * This event is called when a player executes the vanish command to unvanish
     * @param player the player who unvanished
     */
    public PlayerUnvanishedEvent(Player player) {
        super(player);
    }

}
