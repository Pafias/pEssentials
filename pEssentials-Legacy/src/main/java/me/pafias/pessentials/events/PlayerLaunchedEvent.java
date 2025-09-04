package me.pafias.pessentials.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerLaunchedEvent extends PlayerEvent {

    private final HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    private final CommandSender launcher;
    private final Player launched;
    private final double height;

    /***
     * This event is called after a player is launched into the air with this plugin's launch command, by adding a default (1.5) or other specified value to their velocity.
     * Only the y-value changes in the velocity.
     * @param launcher The player or CommandSender who executed the launch command
     * @param launched The player that got launched
     * @param height The height specified (default: 1.5)
     */
    public PlayerLaunchedEvent(CommandSender launcher, Player launched, double height) {
        super(launched);
        this.launcher = launcher;
        this.launched = launched;
        this.height = height;
    }

    /***
     * Get the player or CommandSender that executed the launch command
     * @return the player or CommandSender that executed the launch command
     */
    public CommandSender getWhoLaunched() {
        return launcher;
    }

    /***
     * Get the player who got launched into the air
     * @return the player who got launched into the air
     */
    public Player getLaunched() {
        return launched;
    }

    /***
     * Get the height amount (y-value) that was added to the player velocity vector
     * Default: 1.5
     * @return double height added to player velocity
     */
    public double getHeight() {
        return height;
    }

}
