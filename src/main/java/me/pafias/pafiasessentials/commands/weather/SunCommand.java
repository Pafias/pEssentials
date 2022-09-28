package me.pafias.pafiasessentials.commands.weather;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SunCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cOnly players!"));
            return true;
        }
        if (sender.hasPermission("essentials.sun") && !Arrays.stream(args).anyMatch(arg -> arg.toLowerCase().contains("-p"))) {
            Player player = (Player) sender;
            player.getWorld().setClearWeatherDuration(0);
            player.sendMessage(CC.translate("&6Weather cleared."));
        } else {
            Player player = (Player) sender;
            if (player.getPlayerWeather() != null && player.getPlayerWeather().equals(WeatherType.CLEAR)) {
                player.resetPlayerWeather();
                player.sendMessage(CC.translate("&6Personal weather reset."));
            } else {
                player.setPlayerWeather(WeatherType.CLEAR);
                player.sendMessage(CC.translate("&6Personal weather set to clear"));
            }
        }
        return true;
    }

}
