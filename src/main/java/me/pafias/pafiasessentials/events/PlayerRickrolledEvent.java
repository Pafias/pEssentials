package me.pafias.pafiasessentials.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerRickrolledEvent extends PlayerEvent {

    private final HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    private final CommandSender rick;
    private final Player rolled;
    private final Object npc;

    /***
     * This event is called after a player has been rickrolled with this plugin's rickroll command
     * @param rick the player or CommandSender who executed the rickroll command
     * @param rolled the player who got rickrolled (rip bozo)
     */
    public PlayerRickrolledEvent(CommandSender rick, Player rolled, Object npc) {
        super(rolled);
        this.rick = rick;
        this.rolled = rolled;
        this.npc = npc;
    }

    /***
     * Get the player or CommandSender who executed the rickroll command
     * @return the player or CommandSender who executed the rickroll command
     */
    public CommandSender getWhoRickrolled() {
        return rick;
    }

    /***
     * Get the bozo who got rickrolled
     * @return the player who got rickrolled
     */
    public Player getWhoGotRickrolled() {
        return rolled;
    }

    /***
     * Get the NPC that's being used to simulate Rick Astley.
     * This should only be cast to an EntityPlayer.
     * @return the Rick Astley NPC as an EntityPlayer
     */
    public Object getRickAstleyNPC() {
        return npc;
    }

}
