package me.pafias.pessentials.commands.modules.Player.Weather;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.WeatherType;
import org.bukkit.World;
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
        if (sender.hasPermission("essentials.rain") && Arrays.stream(args).noneMatch(arg -> arg.toLowerCase().contains("-p"))) {
            World world;
            if (args.length >= 1)
                world = plugin.getServer().getWorld(args[0]);
            else {
                world = plugin.getServer().getWorlds().get(0);
            }
            if (world == null) {
                sender.sendMessage(CC.t("&cInvalid world!"));
                return;
            }
            world.setStorm(true);
            sender.sendMessage(CC.t("&6Weather set to storming (rain)."));
        } else if (sender.hasPermission("essentials.rain.personal")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            if (player.getPlayerWeather() != null && player.getPlayerWeather().equals(WeatherType.DOWNFALL)) {
                player.resetPlayerWeather();
                player.sendMessage(CC.t("&6Personal weather reset."));
            } else {
                player.setPlayerWeather(WeatherType.DOWNFALL);
                player.sendMessage(CC.t("&6Personal weather set to raining"));
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }


}
