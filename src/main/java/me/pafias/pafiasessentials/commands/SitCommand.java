package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public class SitCommand implements CommandExecutor {

    public static Map<Entity, ArmorStand> map = new WeakHashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return true;
        }
        if (sender.hasPermission("essentials.sit")) {
            Player player = (Player) sender;
            if (map.containsKey(player)) {
                map.get(player).remove();
                map.remove(player);
            } else {
                Location location = player.getLocation().clone();
                location.subtract(0, player.getHeight(), 0);
                location.add(0, 0.1, 0);
                ArmorStand as = player.getWorld().spawn(location, ArmorStand.class, armorstand -> {
                    ((ArmorStand) armorstand).setGravity(false);
                    ((ArmorStand) armorstand).setVisible(false);
                });
                as.setPassenger(player);
                map.put(player, as);
            }
        }
        return true;
    }

}
