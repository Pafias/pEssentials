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

public class RainCommand extends ICommand {

    public RainCommand() {
        super("rain", null, "Make it rain", "/rain [-p]", "storm", "thunder");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        if (sender.hasPermission("essentials.rain") && !Arrays.stream(args).anyMatch(arg -> arg.toLowerCase().contains("-p"))) {
            Player player = (Player) sender;
            player.getWorld().setStorm(true);
            player.sendMessage(CC.t("&6Weather set to storming (rain)."));
        } else {
            Player player = (Player) sender;
            if (player.getPlayerWeather() != null && player.getPlayerWeather().equals(WeatherType.DOWNFALL)) {
                player.resetPlayerWeather();
                player.sendMessage(CC.t("&6Personal weather reset."));
            } else {
                player.setPlayerWeather(WeatherType.DOWNFALL);
                player.sendMessage(CC.t("&6Personal weather set to raining"));
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }


}
