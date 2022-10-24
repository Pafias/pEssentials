package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public VanishCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.vanish")) {
            Player player;
            if (args.length < 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players can vanish!"));
                    return true;
                }
                player = (Player) sender;
            } else {
                if (sender.hasPermission("essentials.vanish.others")) {
                    player = plugin.getServer().getPlayer(args[0]);
                } else return true;
            }
            if (player == null) {
                sender.sendMessage(CC.t("&cInvalid player!"));
                return true;
            }
            handle(player, sender);
        }
        return true;
    }

    private void handle(Player player, CommandSender sender) {
        if (plugin.getSM().getVanishManager().isVanished(player)) {
            plugin.getSM().getVanishManager().unvanish(player);
            if (sender != player)
                sender.sendMessage(CC.t("&6Vanish for &d" + player.getName() + "&6: &cOFF"));
        } else {
            plugin.getSM().getVanishManager().vanish(player);
            if (sender != player)
                sender.sendMessage(CC.t("&6Vanish for &d" + player.getName() + "&6: &aON"));
        }

    }

}
