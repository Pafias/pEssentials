package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class VanishCommand extends ICommand {

    public VanishCommand() {
        super("vanish", "essentials.vanish", "Vanish", "/vanish [player]", "v");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.vanish")) {
            Player player;
            if (args.length < 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players can vanish!"));
                    return;
                }
                player = (Player) sender;
            } else {
                if (sender.hasPermission("essentials.vanish.others")) {
                    player = plugin.getServer().getPlayer(args[0]);
                } else return;
            }
            if (player == null) {
                sender.sendMessage(CC.t("&cInvalid player!"));
                return;
            }
            handle(player, sender);
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
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
