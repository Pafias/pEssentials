package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        if (sender.hasPermission("essentials.vanish")) {
            Player player;
            if (args.length < 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players can vanish!"));
                    return;
                }
                player = (Player) sender;
            } else {
                if (args[0].equalsIgnoreCase("list")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Set<UUID> vanished = plugin.getSM().getVanishManager().getVanishedPlayers();
                            if (vanished.isEmpty()) {
                                sender.sendMessage(CC.t("&cNo one is in vanish."));
                                return;
                            }
                            StringBuilder sb = new StringBuilder();
                            vanished.forEach(uuid -> {
                                OfflinePlayer player = plugin.getServer().getOfflinePlayer(uuid);
                                sb.append(player.getName()).append("  ");
                            });
                            sender.sendMessage(CC.tf("&6Vanished players: &7%s", sb.toString()));
                        }
                    }.runTaskAsynchronously(plugin);
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
