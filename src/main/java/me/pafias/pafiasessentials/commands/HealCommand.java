package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.events.PlayerHealedEvent;
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
                    sender.sendMessage(CC.t("&cOnly players!"));
                    return true;
                }
                Player player = (Player) sender;
                double oldHealth = player.getHealth();
                player.setHealth(player.getMaxHealth());
                plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(player, player, oldHealth));
                sender.sendMessage(CC.t("&6Healed!"));
            } else {
                if (sender.hasPermission("essentials.heal.others")) {
                    Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(CC.t("&cPlayer not found!"));
                        return true;
                    }
                    double oldHealth = target.getHealth();
                    target.setHealth(target.getMaxHealth());
                    plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(sender, target, oldHealth));
                    target.sendMessage(CC.t("&6You have been healed!"));
                    sender.sendMessage(CC.t("&6Healed &d" + target.getName()));
                }
            }
            return true;
        }
        return true;
    }

}
