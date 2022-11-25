package me.pafias.pafiasessentials.commands.teleport;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.Location;
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
                sender.sendMessage(CC.t("&c/" + label + " <player> [player]"));
                return true;
            } else if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players!"));
                    return true;
                }
                User player = plugin.getSM().getUserManager().getUser((Player) sender);
                User target = plugin.getSM().getUserManager().getUser(args[0]);
                if (target == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return true;
                }
                player.getPlayer().teleport(target.getPlayer());
                sender.sendMessage(CC.t("&6Teleported to &d" + target.getName()));
                return true;
            } else if (args.length == 2) {
                User player1 = plugin.getSM().getUserManager().getUser(args[0]);
                User player2 = plugin.getSM().getUserManager().getUser(args[1]);
                if (player1 == null || player2 == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return true;
                }
                player1.getPlayer().teleport(player2.getPlayer());
                sender.sendMessage(CC.t("&6Teleported &d" + player1.getName() + " &6to &d" + player2.getName()));
                return true;
            } else if (args.length == 3) {
                double x, y, z;
                try {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid coordinates."));
                    return true;
                }
                ((Player) sender).teleport(new Location(((Player) sender).getWorld(), x, y, z));
                sender.sendMessage(CC.tf("&6Teleported to &d%f.2, %f.2, %f.2", x, y, z));
                return true;
            }
        }
        return true;
    }

}
