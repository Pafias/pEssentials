package me.pafias.pessentials.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.objects.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

    private final pEssentials plugin;

    public PAPIExpansion(pEssentials plugin) {
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
                return String.valueOf(user.isVanished());
            case "frozen":
                return String.valueOf(user.isFrozen());
            case "identity":
                return String.valueOf(user.hasIdentity());
        }
        return null;
    }

}