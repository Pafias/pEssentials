package me.pafias.pessentials.commands.modules.Staff.Moderation;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class InvseeCommand extends ICommand {

    public InvseeCommand() {
        super("invsee", "essentials.invsee", "See (and modify) someone's inventory", "/invsee <player>", "inv");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.tf("&c%s", getUsage()));
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || !player.canSee(target)) {
            sender.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        player.openInventory(target.getInventory());
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        return Collections.emptyList();
    }

}
