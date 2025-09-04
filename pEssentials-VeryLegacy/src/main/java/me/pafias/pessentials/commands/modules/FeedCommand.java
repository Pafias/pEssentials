package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FeedCommand extends ICommand {

    public FeedCommand() {
        super("feed", "essentials.feed", "Feed someone", "/feed [player]");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            final Player player = (Player) sender;
            player.setFoodLevel(20);
            sender.sendMessage(CC.t("&6Fed!"));
        } else {
            if (sender.hasPermission("essentials.feed.others")) {
                final boolean silent = Arrays.asList(args).contains("-s");
                if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*")) {
                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                        p.setFoodLevel(20);
                        if (!silent)
                            p.sendMessage(CC.t("&6You have been fed!"));
                    }
                    sender.sendMessage(CC.t("&6Players fed."));
                } else {
                    final Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(CC.t("&cPlayer not found!"));
                        return;
                    }
                    target.setFoodLevel(20);
                    if (!silent)
                        target.sendMessage(CC.t("&6You have been fed!"));
                    sender.sendMessage(CC.t("&6Fed &d" + target.getName()));
                }
            }
        }
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

}
