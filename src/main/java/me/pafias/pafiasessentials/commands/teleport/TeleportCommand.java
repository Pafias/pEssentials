package me.pafias.pafiasessentials.commands.teleport;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public TeleportCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.teleport")) {
            if (args.length == 0) {
                sender.sendMessage(CC.translate("&c/" + label + " <player> [player]"));
                return true;
            } else if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.translate("&cOnly players!"));
                    return true;
                }
                User player = plugin.getSM().getUserManager().getUser((Player) sender);
                User target = plugin.getSM().getUserManager().getUser(args[0]);
                if (target == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found!"));
                    return true;
                }
                player.getPlayer().teleport(target.getPlayer());
                sender.sendMessage(CC.translate("&6Teleported to &d" + target.getName()));
                return true;
            } else {
                User player1 = plugin.getSM().getUserManager().getUser(args[0]);
                User player2 = plugin.getSM().getUserManager().getUser(args[1]);
                if (player1 == null || player2 == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found!"));
                    return true;
                }
                player1.getPlayer().teleport(player2.getPlayer());
                sender.sendMessage(CC.translate("&6Teleported &d" + player1.getName() + " &6to &d" + player2.getName()));
                return true;
            }
        }
        return true;
    }

}
