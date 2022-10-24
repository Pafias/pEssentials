package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {

    private PafiasEssentials plugin;

    public BackCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.back")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return true;
            }
            User player = plugin.getSM().getUserManager().getUser((Player) sender);
            if (player.lastLocation == null) {
                sender.sendMessage(CC.t("&cNo last location available."));
                return true;
            }
            player.getPlayer().teleport(player.lastLocation);
            sender.sendMessage(CC.t("&6Teleported to last location."));
        }
        return true;
    }

}
