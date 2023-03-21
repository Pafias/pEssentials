package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CopyinventoryCommand extends ICommand {

    public CopyinventoryCommand() {
        super("copyinventory", "essentials.copyinventory", "Copy inventory", "/ci <player> [target]", "ci");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.copyinventory")) {
            if (args.length == 0) {
                sender.sendMessage(CC.t("&c/" + label + " <player from> [player to]"));
                return;
            } else if (args.length == 1) {
                Player from = plugin.getServer().getPlayer(args[0]);
                if (from == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players!"));
                    return;
                }
                Player to = (Player) sender;
                copy(from, to);
                sender.sendMessage(CC.t("&aInventory copied!"));
            } else {
                Player from = plugin.getServer().getPlayer(args[0]);
                if (from == null) {
                    sender.sendMessage(CC.t("&cPlayer " + args[0] + " not found!"));
                    return;
                }
                if (args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("*"))
                    plugin.getServer().getOnlinePlayers().forEach(p -> copy(from, p));
                else {
                    Player to = plugin.getServer().getPlayer(args[1]);
                    if (to == null) {
                        sender.sendMessage(CC.t("&cPlayer " + args[1] + " not found!"));
                        return;
                    }
                    copy(from, to);
                }
                sender.sendMessage(CC.t("&aInventory copied!"));
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 3) return Collections.emptyList();
        else return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    private void copy(Player from, Player to) {
        to.getInventory().setStorageContents(from.getInventory().getStorageContents());
        to.getInventory().setArmorContents(from.getInventory().getArmorContents());
        to.getInventory().setExtraContents(from.getInventory().getExtraContents());
    }

}
