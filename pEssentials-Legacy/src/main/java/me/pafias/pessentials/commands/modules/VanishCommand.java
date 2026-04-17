package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import me.pafias.putils.Tasks;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class VanishCommand extends ICommand {

    public VanishCommand() {
        super("vanish", "essentials.vanish", "Vanish", "/vanish [player]", "v");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LCC.t("&cOnly players can vanish!"));
                return;
            }
            player = (Player) sender;
        } else {
            if (args[0].equalsIgnoreCase("list")) {
                Tasks.runAsync(() -> {
                    Set<UUID> vanished = plugin.getSM().getVanishManager().getVanishedPlayers();
                    if (vanished.isEmpty()) {
                        sender.sendMessage(LCC.t("&cNo one is in vanish."));
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    vanished.forEach(uuid -> {
                        OfflinePlayer p = plugin.getServer().getOfflinePlayer(uuid);
                        sb.append(p.getName()).append("  ");
                    });
                    sender.sendMessage(LCC.tf("&6Vanished players: &7%s", sb.toString()));
                });
                return;
            }
            if (sender.hasPermission(getPermission() + ".others")) {
                player = plugin.getServer().getPlayer(args[0]);
            } else return;
        }
        if (player == null || (sender instanceof Player && !((Player) sender).canSee(player))) {
            sender.sendMessage(LCC.t("&cInvalid player!"));
            return;
        }
        handle(player, sender);
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        return Collections.emptyList();
    }

    private void handle(Player player, CommandSender sender) {
        if (plugin.getSM().getVanishManager().isVanished(player)) {
            plugin.getSM().getVanishManager().unvanish(player);
            if (sender != player)
                sender.sendMessage(LCC.t("&6Vanish for &d" + player.getName() + "&6: &cOFF"));
        } else {
            plugin.getSM().getVanishManager().vanish(player);
            if (sender != player)
                sender.sendMessage(LCC.t("&6Vanish for &d" + player.getName() + "&6: &aON"));
        }

    }

}
