package me.pafias.pessentials.events;

import me.pafias.pessentials.objects.User;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerFrozenEvent extends PlayerEvent {

    private final HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    private final User frozen;
    private final CommandSender freezer;

    /***
     * This event is called when a player gets frozen
     * @param frozen:  the user who got frozen
     * @param freezer: the command sender that executed the command
     */
    public PlayerFrozenEvent(User frozen, CommandSender freezer) {
        super(frozen.getPlayer());
        this.frozen = frozen;
        this.freezer = freezer;
    }

    public User getFrozen() {
        return frozen;
    }

    public CommandSender getFreezer() {
        return freezer;
    }

}
