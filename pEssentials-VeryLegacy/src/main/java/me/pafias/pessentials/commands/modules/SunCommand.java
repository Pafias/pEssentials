package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.RandomUtils;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SunCommand extends ICommand {

    public SunCommand() {
        super("sun", null, "Make it sun", "/sun [-p]", "sunny");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        if (sender.hasPermission("essentials.sun") && !RandomUtils.containsIgnoreCase(args, "-p")) {
            final Player player = (Player) sender;
            player.getWorld().setWeatherDuration(0);
            player.getWorld().setStorm(false);
            player.getWorld().setThundering(false);
            player.sendMessage(CC.t("&6Weather cleared."));
        } else if (sender.hasPermission("essentials.sun.personal")) {
            final Player player = (Player) sender;
            if (player.getPlayerWeather() != null && player.getPlayerWeather().equals(WeatherType.CLEAR)) {
                player.resetPlayerWeather();
                player.sendMessage(CC.t("&6Personal weather reset."));
            } else {
                player.setPlayerWeather(WeatherType.CLEAR);
                player.sendMessage(CC.t("&6Personal weather set to clear"));
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
