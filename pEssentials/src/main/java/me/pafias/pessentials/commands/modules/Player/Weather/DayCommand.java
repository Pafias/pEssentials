package me.pafias.pessentials.commands.modules.Player.Weather;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DayCommand extends ICommand {

    public DayCommand() {
        super("day", null, "Make it day", "/day", "daytime");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.day") && Arrays.stream(args).noneMatch(arg -> arg.toLowerCase().contains("-p"))) {
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
            world.setTime(0);
            sender.sendMessage(CC.t("&6Time set to day (0 ticks) on world " + world.getName()));
        } else if (sender.hasPermission("essentials.day.personal")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            if (!player.isPlayerTimeRelative()) {
                player.resetPlayerTime();
                player.sendMessage(CC.t("&6Personal time reset."));
            } else {
                player.setPlayerTime(0, false);
                player.sendMessage(CC.t("&6Personal time set to day (0 ticks)"));
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
