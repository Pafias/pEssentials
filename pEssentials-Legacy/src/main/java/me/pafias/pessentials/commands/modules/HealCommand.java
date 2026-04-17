package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.events.PlayerHealedEvent;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HealCommand extends ICommand {

    public HealCommand() {
        super("heal", "essentials.heal", "Heal someone", "/heal [player]");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LCC.t("&cOnly players!"));
                return;
            }
            final Player player = (Player) sender;
            final double oldHealth = player.getHealth();
            player.setHealth(player.getMaxHealth());
            plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(player, player, oldHealth));
            sender.sendMessage(LCC.t("&6Healed!"));
        } else {
            if (sender.hasPermission(getPermission() + ".others")) {
                final boolean silent = Arrays.asList(args).contains("-s");
                if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*")) {
                    plugin.getServer().getOnlinePlayers().forEach(p -> {
                        final double oldHealth = p.getHealth();
                        p.setHealth(p.getMaxHealth());
                        plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(sender, p, oldHealth));
                        if (!silent)
                            p.sendMessage(LCC.t("&6You have been healed!"));
                    });
                    sender.sendMessage(LCC.t("&6Players healed."));
                } else {
                    final Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
                        sender.sendMessage(LCC.t("&cPlayer not found!"));
                        return;
                    }
                    final double oldHealth = target.getHealth();
                    target.setHealth(target.getMaxHealth());
                    plugin.getServer().getPluginManager().callEvent(new PlayerHealedEvent(sender, target, oldHealth));
                    if (!silent)
                        target.sendMessage(LCC.t("&6You have been healed!"));
                    sender.sendMessage(LCC.t("&6Healed &d" + target.getName()));
                }
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        else return Collections.emptyList();
    }

}
