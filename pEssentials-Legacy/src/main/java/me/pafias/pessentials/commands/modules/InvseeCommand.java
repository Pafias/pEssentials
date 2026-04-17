package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
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
            sender.sendMessage(LCC.tf("&c%s", getUsage()));
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(LCC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        final Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || !player.canSee(target)) {
            sender.sendMessage(LCC.t("&cPlayer not found!"));
            return;
        }
        player.openInventory(target.getInventory());
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        return Collections.emptyList();
    }

}
