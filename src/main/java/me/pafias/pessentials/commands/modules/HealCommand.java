package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.events.PlayerHealedEvent;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HealCommand extends ICommand {

    public HealCommand() {
        super("heal", "essentials.heal", "Heal someone", "/heal [player]");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            final Player player = (Player) sender;
            final double oldHealth = player.getHealth();
            player.setHealth(player.getMaxHealth());
            plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(player, player, oldHealth));
            sender.sendMessage(CC.t("&6Healed!"));
        } else {
            if (sender.hasPermission("essentials.heal.others")) {
                final boolean silent = Arrays.asList(args).contains("-s");
                if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*")) {
                    plugin.getServer().getOnlinePlayers().forEach(p -> {
                        final double oldHealth = p.getHealth();
                        p.setHealth(p.getMaxHealth());
                        plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(sender, p, oldHealth));
                        if (!silent)
                            p.sendMessage(CC.t("&6You have been healed!"));
                    });
                    sender.sendMessage(CC.t("&6Players healed."));
                } else {
                    final Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(CC.t("&cPlayer not found!"));
                        return;
                    }
                    final double oldHealth = target.getHealth();
                    target.setHealth(target.getMaxHealth());
                    plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(sender, target, oldHealth));
                    if (!silent)
                        target.sendMessage(CC.t("&6You have been healed!"));
                    sender.sendMessage(CC.t("&6Healed &d" + target.getName()));
                }
            }
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
                    .collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
