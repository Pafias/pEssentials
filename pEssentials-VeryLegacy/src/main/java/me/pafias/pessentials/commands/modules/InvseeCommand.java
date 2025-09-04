package me.pafias.pessentials.commands.modules;

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
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        final Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        player.openInventory(target.getInventory());
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
