package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.events.PlayerHealedEvent;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HealCommand extends ICommand {

    public HealCommand() {
        super("heal", "essentials.heal", "Heal someone", "/heal [player]");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.heal")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players!"));
                    return;
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
                        return;
                    }
                    double oldHealth = target.getHealth();
                    target.setHealth(target.getMaxHealth());
                    plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(sender, target, oldHealth));
                    target.sendMessage(CC.t("&6You have been healed!"));
                    sender.sendMessage(CC.t("&6Healed &d" + target.getName()));
                }
            }
            return;
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream().filter(p -> ((Player) sender).canSee(p)).map(Player::getName).filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
