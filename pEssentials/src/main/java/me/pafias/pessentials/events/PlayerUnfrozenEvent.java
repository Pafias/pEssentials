package me.pafias.pessentials.events;

import lombok.Getter;
import me.pafias.pessentials.objects.User;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerUnfrozenEvent extends PlayerEvent {

    private final HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    @Getter
    private final User unfrozen;

    @Getter
    private final CommandSender unfreezer;

    /***
     * This event is called when a player gets unfrozen
     * @param unfrozen:  the user who got unfrozen
     * @param unfreezer: the command sender that executed the command
     */
    public PlayerUnfrozenEvent(User unfrozen, CommandSender unfreezer) {
        super(unfrozen.getPlayer());
        this.unfrozen = unfrozen;
        this.unfreezer = unfreezer;
    }

}
