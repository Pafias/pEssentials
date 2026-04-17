package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
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
                sender.sendMessage(LCC.t("&cOnly players!"));
                return;
            }
            final Player player = (Player) sender;
            player.setFoodLevel(20);
            sender.sendMessage(LCC.t("&6Fed!"));
        } else {
            if (sender.hasPermission(getPermission() + ".others")) {
                final boolean silent = Arrays.asList(args).contains("-s");
                if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*")) {
                    plugin.getServer().getOnlinePlayers().forEach(p -> {
                        p.setFoodLevel(20);
                        if (!silent)
                            p.sendMessage(LCC.t("&6You have been fed!"));
                    });
                    sender.sendMessage(LCC.t("&6Players fed."));
                } else {
                    final Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
                        sender.sendMessage(LCC.t("&cPlayer not found!"));
                        return;
                    }
                    target.setFoodLevel(20);
                    if (!silent)
                        target.sendMessage(LCC.t("&6You have been fed!"));
                    sender.sendMessage(LCC.t("&6Fed &d" + target.getName()));
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
