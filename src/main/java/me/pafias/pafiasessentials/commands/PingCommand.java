package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public PingCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    private int getPing(Player player) {
        return plugin.getSM().getNMSProvider().getPing(player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (sender.hasPermission("essentials.ping")) {
            if (args.length == 0) {
                Player player = (Player) sender;
                int ping = getPing(player);
                String sping = (ping < 30 ? ChatColor.DARK_GREEN.toString() + ping : ping < 50 ? ChatColor.GREEN.toString() + ping : ping < 100 ? ChatColor.YELLOW.toString() + ping : ChatColor.RED.toString() + ping);
                player.sendMessage(CC.t("&6Ping: " + sping + "&7ms"));
            } else {
                if (sender.hasPermission("essentials.ping.others")) {
                    Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(CC.t("&cPlayer not found."));
                        return true;
                    }
                    int ping = getPing(target);
                    String sping = (ping < 30 ? ChatColor.DARK_GREEN.toString() + ping : ping < 50 ? ChatColor.GREEN.toString() + ping : ping < 100 ? ChatColor.YELLOW.toString() + ping : ChatColor.RED.toString() + ping);
                    sender.sendMessage(CC.t("&d" + target.getName() + (target.getName().endsWith("s") ? "&6'" : "&6's") + " &6ping: " + sping + "&7ms"));
                }
            }
        }
        return true;
    }

}
