package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class SitCommand extends ICommand {

    public static Map<Entity, ArmorStand> map = new WeakHashMap<>();

    public SitCommand() {
        super("sit", "essentials.sit", "Sit down", "/sit", "chair");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
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
                ArmorStand as = player.getWorld().spawn(location, ArmorStand.class);
                as.setGravity(false);
                as.setVisible(false);
                as.setPassenger(player);
                map.put(player, as);
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
