package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TeleporthereCommand extends ICommand {

    public TeleporthereCommand() {
        super("teleporthere", "essentials.tphere", "Teleport someone to you", "/tphere <player>/all", "tphere");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.t("&c/" + label + " <player/all>"));
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final User player1 = plugin.getSM().getUserManager().getUser((Player) sender);
        if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*")) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                p.teleport(player1.getPlayer());
            }
            sender.sendMessage(CC.t("&6Teleported everyone to you"));
        } else {
            final User player2 = plugin.getSM().getUserManager().getUser(args[0]);
            if (player2 == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            player2.getPlayer().teleport(player1.getPlayer());
            sender.sendMessage(CC.t("&6Teleported &d" + player2.getName() + " &6to you"));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> names = new java.util.ArrayList<>();
            String prefix = args[0].toLowerCase();
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
