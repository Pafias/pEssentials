package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FeedCommand extends ICommand {

    public FeedCommand() {
        super("feed", "essentials.feed", "Feed someone", "/feed [player]");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.feed")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players!"));
                    return;
                }
                Player player = (Player) sender;
                player.setFoodLevel(20);
                sender.sendMessage(CC.t("&6Fed!"));
            } else {
                if (sender.hasPermission("essentials.feed.others")) {
                    boolean silent = Arrays.asList(args).contains("-s");
                    if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*")) {
                        plugin.getServer().getOnlinePlayers().forEach(p -> {
                            p.setFoodLevel(20);
                            if (!silent)
                                p.sendMessage(CC.t("&6You have been fed!"));
                        });
                        sender.sendMessage(CC.t("&6Players fed."));
                    } else {
                        Player target = plugin.getServer().getPlayer(args[0]);
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
            return;
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
