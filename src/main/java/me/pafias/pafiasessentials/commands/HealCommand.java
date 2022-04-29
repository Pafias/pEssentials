package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    private PafiasEssentials plugin;

    public HealCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.heal")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.translate("&cOnly players!"));
                    return true;
                }
                Player player = (Player) sender;
                player.setHealth(player.getMaxHealth());
                sender.sendMessage(CC.translate("&6Healed!"));
            } else {
                if (sender.hasPermission("essentials.heal.others")) {
                    Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(CC.translate("&cPlayer not found!"));
                        return true;
                    }
                    target.setHealth(target.getMaxHealth());
                    target.sendMessage(CC.translate("&6You have been healed!"));
                    sender.sendMessage(CC.translate("&6Healed &d" + target.getName()));
                }
            }
            return true;
        }
        return true;
    }

}
