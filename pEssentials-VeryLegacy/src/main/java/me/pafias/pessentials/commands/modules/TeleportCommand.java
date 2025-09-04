package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TeleportCommand extends ICommand {

    public TeleportCommand() {
        super("teleport", "essentials.teleport", "Teleportation", "/tp <player> [player]", "tp");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            sender.sendMessage(CC.t("&c/" + label + " <player> [player]"));
        else if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            final User player = plugin.getSM().getUserManager().getUser((Player) sender);
            final User target = plugin.getSM().getUserManager().getUser(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            player.getPlayer().teleport(target.getPlayer());
            sender.sendMessage(CC.t("&6Teleported to &d" + target.getName()));
        } else if (args.length == 2) {
            final User player1 = plugin.getSM().getUserManager().getUser(args[0]);
            final User player2 = plugin.getSM().getUserManager().getUser(args[1]);
            if (player1 == null || player2 == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            player1.getPlayer().teleport(player2.getPlayer());
            sender.sendMessage(CC.t("&6Teleported &d" + player1.getName() + " &6to &d" + player2.getName()));
        } else if (args.length == 3) {
            double x, y, z;
            try {
                x = Double.parseDouble(args[0]);
                y = Double.parseDouble(args[1]);
                z = Double.parseDouble(args[2]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(CC.t("&cInvalid coordinates."));
                return;
            }
            ((Player) sender).teleport(new Location(((Player) sender).getWorld(), x, y, z));
            sender.sendMessage(CC.tf("&6Teleported to &d%f.2, %f.2, %f.2", x, y, z));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 || args.length == 2) {
            List<String> names = new java.util.ArrayList<>();
            String prefix = args[args.length - 1].toLowerCase();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String n = p.getName();
                    if (n != null && n.toLowerCase().startsWith(prefix))
                        names.add(n);
                }
            }
            return names;
        } else return Collections.emptyList();
    }

}
