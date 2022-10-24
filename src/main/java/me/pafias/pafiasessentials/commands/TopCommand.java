package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.top")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return true;
            }
            Player player = (Player) sender;
            Block highest = player.getLocation().getWorld().getHighestBlockAt(player.getLocation());
            if (highest == null) {
                player.sendMessage(CC.t("&cNo highest block found."));
                return true;
            }
            player.teleport(highest.getLocation());
            player.sendMessage(CC.t("&6Teleported to the highest block!"));
        }
        return true;
    }

}
