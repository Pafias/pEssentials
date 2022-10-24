package me.pafias.pafiasessentials.commands.teleport;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TphereCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public TphereCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.teleporthere") || sender.hasPermission("essentials.tphere")) {
            if (args.length < 1) {
                sender.sendMessage(CC.t("&c/" + label + " <player/all>"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return true;
            }
            User player1 = plugin.getSM().getUserManager().getUser((Player) sender);
            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                plugin.getServer().getOnlinePlayers().forEach(p -> {
                    p.teleport(player1.getPlayer());
                });
                sender.sendMessage(CC.t("&6Teleported everyone to you"));
                return true;
            } else {
                User player2 = plugin.getSM().getUserManager().getUser(args[0]);
                if (player2 == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return true;
                }
                player2.getPlayer().teleport(player1.getPlayer());
                sender.sendMessage(CC.t("&6Teleported &d" + player2.getName() + " &6to you"));
                return true;
            }
        }
        return true;
    }

}
