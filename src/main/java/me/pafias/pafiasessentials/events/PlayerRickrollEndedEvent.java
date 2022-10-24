package me.pafias.pafiasessentials.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerRickrollEndedEvent extends PlayerEvent {

    private final HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    private final CommandSender rick;
    private final Player rolled;

    /***
     * This event is called when a rickroll ends
     * @param rick the player or CommandSender who had executed the rickroll command
     * @param rolled the player who had gotten rickrolled (rip bozo)
     */
    public PlayerRickrollEndedEvent(CommandSender rick, Player rolled) {
        super(rolled);
        this.rick = rick;
        this.rolled = rolled;
    }

    /***
     * Get the player or CommandSender who had executed the rickroll command
     * @return the player or CommandSender who had executed the rickroll command
     */
    public CommandSender getWhoRickrolled() {
        return rick;
    }

    /***
     * Get the bozo who had gotten rickrolled
     * @return the player who had gotten rickrolled
     */
    public Player getWhoGotRickrolled() {
        return rolled;
    }

}
