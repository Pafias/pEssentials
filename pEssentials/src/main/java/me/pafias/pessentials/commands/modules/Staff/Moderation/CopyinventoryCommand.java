package me.pafias.pessentials.commands.modules.Staff.Moderation;

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
            if (from == null || (sender instanceof Player senderPlayer && !senderPlayer.canSee(from))) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            if (!(sender instanceof Player to)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            copy(from, to);
            sender.sendMessage(CC.t("&aInventory copied!"));
        } else {
            final Player from = plugin.getServer().getPlayer(args[0]);
            if (from == null || (sender instanceof Player senderPlayer && !senderPlayer.canSee(from))) {
                sender.sendMessage(CC.t("&cPlayer " + args[0] + " not found!"));
                return;
            }
            if (args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("*"))
                plugin.getServer().getOnlinePlayers().forEach(p -> copy(from, p));
            else {
                final Player to = plugin.getServer().getPlayer(args[1]);
                if (to == null || (sender instanceof Player senderPlayer && !senderPlayer.canSee(to))) {
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
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        else if (args.length == 2) {
            return plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        } else
            return Collections.emptyList();
    }

    private void copy(Player from, Player to) {
        try {
            to.getInventory().setStorageContents(from.getInventory().getStorageContents());
            to.getInventory().setArmorContents(from.getInventory().getArmorContents());
            to.getInventory().setExtraContents(from.getInventory().getExtraContents());
        } catch (Throwable t) {
            to.getInventory().setContents(from.getInventory().getContents());
            to.getInventory().setArmorContents(from.getInventory().getArmorContents());
        }
    }

}
