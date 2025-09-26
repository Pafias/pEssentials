package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PingCommand extends ICommand {

    public PingCommand() {
        super("ping", "essentials.ping", "Ping", "/ping [player]", "latency");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length == 0 && sender instanceof Player) {
            target = (Player) sender;
        } else if (sender.hasPermission(getPermission() + ".others"))
            target = plugin.getServer().getPlayer(args[0]);
        else {
            sender.sendMessage(CC.t("&cYou don't have permission to check other players' ping ;("));
            return;
        }
        if (target == null || (sender instanceof Player senderPlayer && !senderPlayer.canSee(target))) {
            sender.sendMessage(CC.t("&cPlayer not found."));
            return;
        }
        final int ping = target.getPing();
        final String pingColored = (ping < 30 ? ChatColor.DARK_GREEN.toString() + ping : ping < 50 ? ChatColor.GREEN.toString() + ping : ping < 100 ? ChatColor.YELLOW.toString() + ping : ChatColor.RED.toString() + ping);
        final String pingMsg;
        if (target == sender)
            pingMsg = CC.t("&6Ping: " + pingColored + "&7ms");
        else
            pingMsg = CC.t("&d" + target.getName() + (target.getName().endsWith("s") ? "&6'" : "&6's") + " &6ping: " + pingColored + "&7ms");
        sender.sendMessage(pingMsg);
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        else return Collections.emptyList();
    }

}
