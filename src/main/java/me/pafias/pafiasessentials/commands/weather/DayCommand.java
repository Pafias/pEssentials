package me.pafias.pafiasessentials.commands.weather;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class DayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cOnly players!"));
            return true;
        }
        if (sender.hasPermission("essentials.day") && !Arrays.stream(args).anyMatch(arg -> arg.toLowerCase().contains("-p"))) {
            Player player = (Player) sender;
            player.getWorld().setTime(0);
            player.sendMessage(CC.translate("&6Time set to day (0 ticks)"));
        } else {
            Player player = (Player) sender;
            if (!player.isPlayerTimeRelative()) {
                player.resetPlayerTime();
                player.sendMessage(CC.translate("&6Personal time reset."));
            } else {
                player.setPlayerTime(0, false);
                player.sendMessage(CC.translate("&6Personal time set to day (0 ticks)"));
            }
        }
        return true;
    }

}
