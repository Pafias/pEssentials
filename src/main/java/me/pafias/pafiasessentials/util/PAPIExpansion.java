package me.pafias.pafiasessentials.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

    private final PafiasEssentials plugin;

    public PAPIExpansion(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "essentials";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Pafias";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";
        User user = plugin.getSM().getUserManager().getUser(player);
        if (user == null) return "";
        switch (params) {
            case "name":
                return user.getRealName();
            case "nickname":
                return user.getName();
            case "staffchat":
                return String.valueOf(user.isInStaffChat());
            case "vanished":
                return String.valueOf(plugin.getSM().getVanishManager().isVanished(player));
        }
        return null;
    }

}