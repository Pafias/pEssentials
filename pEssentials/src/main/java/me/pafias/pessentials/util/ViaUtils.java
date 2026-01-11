package me.pafias.pessentials.util;

import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;

public class ViaUtils {

    public static boolean isAtLeast1_20_6(Player player) {
        try {
            return Via.getAPI().getPlayerVersion(player.getUniqueId()) >= 767;
        } catch (Exception e) {
            return false;
        }
    }

}