package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    private PafiasEssentials plugin;

    public FeedCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.feed")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players!"));
                    return true;
                }
                Player player = (Player) sender;
                player.setFoodLevel(20);
                sender.sendMessage(CC.t("&6Fed!"));
            } else {
                if (sender.hasPermission("essentials.heal.others")) {
                    Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(CC.t("&cPlayer not found!"));
                        return true;
                    }
                    target.setFoodLevel(20);
                    target.sendMessage(CC.t("&6You have been fed!"));
                    sender.sendMessage(CC.t("&6Fed &d" + target.getName()));
                }
            }
            return true;
        }
        return true;
    }

}
