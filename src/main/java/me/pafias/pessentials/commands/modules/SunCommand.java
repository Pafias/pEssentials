package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SunCommand extends ICommand {

    public SunCommand() {
        super("sun", "essentials.sun", "Make it sun", "/sun [-p]", "sunny");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        if (sender.hasPermission("essentials.sun") && Arrays.stream(args).noneMatch(arg -> arg.toLowerCase().contains("-p"))) {
            Player player = (Player) sender;
            try {
                player.getWorld().setClearWeatherDuration(0);
            } catch (Throwable ignored) {
            }
            player.getWorld().setThundering(false);
            player.sendMessage(CC.t("&6Weather cleared."));
        } else {
            Player player = (Player) sender;
            if (player.getPlayerWeather() != null && player.getPlayerWeather().equals(WeatherType.CLEAR)) {
                player.resetPlayerWeather();
                player.sendMessage(CC.t("&6Personal weather reset."));
            } else {
                player.setPlayerWeather(WeatherType.CLEAR);
                player.sendMessage(CC.t("&6Personal weather set to clear"));
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
