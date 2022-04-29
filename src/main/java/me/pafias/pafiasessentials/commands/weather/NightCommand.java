package me.pafias.pafiasessentials.commands.weather;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NightCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.night")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cOnly players!"));
                return true;
            }
            Player player = (Player) sender;
            player.getWorld().setTime(14000);
            player.sendMessage(CC.translate("&6Time set to night (14000)"));
        }
        return true;
    }

}
