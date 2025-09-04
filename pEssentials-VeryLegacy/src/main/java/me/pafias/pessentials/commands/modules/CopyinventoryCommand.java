package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CopyinventoryCommand extends ICommand {

    public CopyinventoryCommand() {
        super("copyinventory", "essentials.copyinventory", "Copy inventory", "/ci <player> [target]", "copyinv", "ci");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            sender.sendMessage(CC.t("&c/" + label + " <player from> [player to]"));
        else if (args.length == 1) {
            final Player from = plugin.getServer().getPlayer(args[0]);
            if (from == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            final Player to = (Player) sender;
            copy(from, to);
            sender.sendMessage(CC.t("&aInventory copied!"));
        } else {
            final Player from = plugin.getServer().getPlayer(args[0]);
            if (from == null) {
                sender.sendMessage(CC.t("&cPlayer " + args[0] + " not found!"));
                return;
            }
            if (args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("*"))
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    copy(from, p);
                }
            else {
                final Player to = plugin.getServer().getPlayer(args[1]);
                if (to == null) {
                    sender.sendMessage(CC.t("&cPlayer " + args[1] + " not found!"));
                    return;
                }
                copy(from, to);
            }
            sender.sendMessage(CC.t("&aInventory copied!"));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> names = new java.util.ArrayList<String>();
            String prefix = args[0].toLowerCase();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String n = p.getName();
                    if (n != null && n.toLowerCase().startsWith(prefix))
                        names.add(n);
                }
            }
            return names;
        } else if (args.length == 2) {
            List<String> names = new java.util.ArrayList<String>();
            String prefix = args[1].toLowerCase();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String n = p.getName();
                    if (n != null && n.toLowerCase().startsWith(prefix))
                        names.add(n);
                }
            }
            return names;
        } else
            return Collections.emptyList();
    }

    private void copy(Player from, Player to) {
        to.getInventory().setContents(from.getInventory().getContents());
        to.getInventory().setArmorContents(from.getInventory().getArmorContents());
    }

}
