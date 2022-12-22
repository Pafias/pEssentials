package me.pafias.pafiasessentials.util;

import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class RandomUtils {

    private static final PafiasEssentials plugin = PafiasEssentials.get();

    public static Set<Player> getStaffOnline(String perm) {
        return plugin.getServer().getOnlinePlayers().stream().filter(p -> p.hasPermission(perm)).collect(Collectors.toSet());
    }

}
