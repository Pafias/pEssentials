package me.pafias.pafiasessentials.commands.weather;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.day")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cOnly players!"));
                return true;
            }
            Player player = (Player) sender;
            player.getWorld().setTime(0);
            player.sendMessage(CC.translate("&6Time set to day (0)"));
        }
        return true;
    }

}
