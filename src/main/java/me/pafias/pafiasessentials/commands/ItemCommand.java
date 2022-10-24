package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.item")) {
            StringBuilder sb = new StringBuilder();
            for (String arg : args)
                sb.append(arg).append(" ");
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return true;
            }
            Player player = (Player) sender;
            player.performCommand("give " + player.getName() + " " + sb.toString());
        }
        return true;
    }

}
