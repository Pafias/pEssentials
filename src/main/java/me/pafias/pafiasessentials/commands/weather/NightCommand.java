package me.pafias.pafiasessentials.commands.weather;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class NightCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return true;
        }
        if (sender.hasPermission("essentials.night") && !Arrays.stream(args).anyMatch(arg -> arg.toLowerCase().contains("-p"))) {
            Player player = (Player) sender;
            player.getWorld().setTime(14000);
            player.sendMessage(CC.t("&6Time set to night (14000 ticks)"));
        } else {
            Player player = (Player) sender;
            if (!player.isPlayerTimeRelative()) {
                player.resetPlayerTime();
                player.sendMessage(CC.t("&6Personal time reset."));
            } else {
                player.setPlayerTime(14000, false);
                player.sendMessage(CC.t("&6Personal time set to night (14000 ticks)"));
            }
        }
        return true;
    }

}
