package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class VanishCommand extends ICommand {

    public VanishCommand() {
        super("vanish", "essentials.vanish", "Vanish", "/vanish [player]", "v");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players can vanish!"));
                return;
            }
            player = (Player) sender;
        } else {
            if (args[0].equalsIgnoreCase("list")) {
                final Set<UUID> vanished = plugin.getSM().getVanishManager().getVanishedPlayers();
                if (vanished.isEmpty()) {
                    sender.sendMessage(CC.t("&cNo one is in vanish."));
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                for (final UUID uuid : vanished) {
                    final Player p = plugin.getServer().getPlayer(uuid);
                    if (p != null)
                        sb.append(p.getName()).append("  ");
                }
                sender.sendMessage(CC.tf("&6Vanished players: &7%s", sb.toString()));
                return;
            }
            if (sender.hasPermission(getPermission() + ".others")) {
                player = plugin.getServer().getPlayer(args[0]);
            } else return;
        }
        if (player == null) {
            sender.sendMessage(CC.t("&cInvalid player!"));
            return;
        }
        handle(player, sender);
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
