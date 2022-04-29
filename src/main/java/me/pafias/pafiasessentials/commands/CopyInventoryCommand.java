package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CopyInventoryCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public CopyInventoryCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.copyinventory")) {
            if (args.length == 0) {
                sender.sendMessage(CC.translate("&c/" + label + " <player from> [player to]"));
                return true;
            } else if (args.length == 1) {
                Player from = plugin.getServer().getPlayer(args[0]);
                if (from == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found!"));
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.translate("&cOnly players!"));
                    return true;
                }
                Player to = (Player) sender;
                copy(from, to);
                sender.sendMessage(CC.translate("&aInventory copied!"));
            } else {
                Player from = plugin.getServer().getPlayer(args[0]);
                if (from == null) {
                    sender.sendMessage(CC.translate("&cPlayer " + args[0] + " not found!"));
                    return true;
                }
                Player to = plugin.getServer().getPlayer(args[1]);
                if (to == null) {
                    sender.sendMessage(CC.translate("&cPlayer " + args[1] + " not found!"));
                    return true;
                }
                copy(from, to);
                sender.sendMessage(CC.translate("&aInventory copied!"));
            }
        }
        return true;
    }

    private void copy(Player from, Player to) {
        to.getInventory().setStorageContents(from.getInventory().getStorageContents());
        to.getInventory().setArmorContents(from.getInventory().getArmorContents());
        to.getInventory().setExtraContents(from.getInventory().getExtraContents());
    }

}
