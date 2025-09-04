package me.pafias.pessentials.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.services.UserManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

    private final UserManager userManager;

    public PAPIExpansion(UserManager userManager) {
        this.userManager = userManager;
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
        final User user = userManager.getUser(player);
        if (user == null) return "";
        switch (params) {
            case "name":
            case "nickname":
                return user.getName();
            case "staffchat":
                return String.valueOf(user.isInStaffchat());
            case "vanished":
                return String.valueOf(user.isVanished());
            case "frozen":
                return String.valueOf(user.isFrozen());
        }
        return null;
    }

}