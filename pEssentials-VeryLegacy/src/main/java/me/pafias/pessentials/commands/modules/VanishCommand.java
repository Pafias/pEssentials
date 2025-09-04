package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Tasks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class VanishCommand extends ICommand {

    public VanishCommand() {
        super("vanish", "essentials.vanish", "Vanish", "/vanish [player]", "v");
    }

    @Override
    public void commandHandler(final CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players can vanish!"));
                return;
            }
            player = (Player) sender;
        } else {
            if (args[0].equalsIgnoreCase("list")) {
                Tasks.runAsync(new BukkitRunnable() {
                    @Override
                    public void run() {
                        Set<Player> vanished = plugin.getSM().getVanishManager().getVanishedPlayers();
                        if (vanished.isEmpty()) {
                            sender.sendMessage(CC.t("&cNo one is in vanish."));
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (Player p : vanished) {
                            sb.append(p.getName()).append("  ");
                        }
                        sender.sendMessage(CC.tf("&6Vanished players: &7%s", sb.toString()));
                    }
                });
                return;
            }
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

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> names = new java.util.ArrayList<>();
            String prefix = args[0].toLowerCase();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String n = p.getName();
                    if (n != null && n.toLowerCase().startsWith(prefix))
                        names.add(n);
                }
            }
            return names;
        } else return Collections.emptyList();
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
