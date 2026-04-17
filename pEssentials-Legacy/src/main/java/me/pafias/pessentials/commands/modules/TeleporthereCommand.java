package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TeleporthereCommand extends ICommand {

    public TeleporthereCommand() {
        super("teleporthere", "essentials.tphere", "Teleport someone to you", "/tphere <player>/all", "tphere");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(LCC.t("&c/" + label + " <player/all>"));
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(LCC.t("&cOnly players!"));
            return;
        }
        final User player1 = plugin.getSM().getUserManager().getUser((Player) sender);
        if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*")) {
            plugin.getServer().getOnlinePlayers().forEach(p -> p.teleport(player1.getPlayer()));
            sender.sendMessage(LCC.t("&6Teleported everyone to you"));
        } else {
            final User player2 = plugin.getSM().getUserManager().getUser(args[0]);
            if (player2 == null || !player1.getPlayer().canSee(player2.getPlayer())) {
                sender.sendMessage(LCC.t("&cPlayer not found!"));
                return;
            }
            player2.getPlayer().teleport(player1.getPlayer());
            sender.sendMessage(LCC.t("&6Teleported &d" + player2.getName() + " &6to you"));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        else return Collections.emptyList();
    }

}
