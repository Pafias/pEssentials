package me.pafias.pessentials.util;

import me.pafias.pessentials.pEssentials;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class RandomUtils {

    private static final pEssentials plugin = pEssentials.get();

    public static Set<Player> getStaffOnline(String perm) {
        return plugin.getServer().getOnlinePlayers().stream().filter(p -> p.hasPermission(perm)).collect(Collectors.toSet());
    }

}
