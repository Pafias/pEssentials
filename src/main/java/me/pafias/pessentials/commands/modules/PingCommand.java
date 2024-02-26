package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PingCommand extends ICommand {

    public PingCommand() {
        super("ping", null, "Ping", "/ping [player]", "latency");
    }

    private int getPing(Player player) {
        return Reflection.getPing(player);
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return;
        if (args.length == 0) {
            Player player = (Player) sender;
            int ping = getPing(player);
            String sping = (ping < 30 ? ChatColor.DARK_GREEN.toString() + ping : ping < 50 ? ChatColor.GREEN.toString() + ping : ping < 100 ? ChatColor.YELLOW.toString() + ping : ChatColor.RED.toString() + ping);
            player.sendMessage(CC.t("&6Ping: " + sping + "&7ms"));
        } else {
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found."));
                return;
            }
            int ping = getPing(target);
            String sping = (ping < 30 ? ChatColor.DARK_GREEN.toString() + ping : ping < 50 ? ChatColor.GREEN.toString() + ping : ping < 100 ? ChatColor.YELLOW.toString() + ping : ChatColor.RED.toString() + ping);
            sender.sendMessage(CC.t("&d" + target.getName() + (target.getName().endsWith("s") ? "&6'" : "&6's") + " &6ping: " + sping + "&7ms"));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
