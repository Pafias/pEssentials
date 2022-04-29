package me.pafias.pafiasessentials.commands.messaging;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TellCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public TellCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    public static Map<UUID, UUID> msg = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&c/" + label + " <player> <message>"));
            return true;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cOnly players!"));
                return true;
            }
            User player = plugin.getSM().getUserManager().getUser((Player) sender);
            User target = plugin.getSM().getUserManager().getUser(args[0]);
            if (target == null) {
                sender.sendMessage(CC.translate("&cPlayer not found!"));
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            String message = sb.toString();
            target.getPlayer().sendMessage(CC.translate("&e[Tell] &c" + player.getName() + "&6: &r" + message));
            player.getPlayer().sendMessage(CC.translate("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));
            msg.put(player.getUUID(), target.getUUID());
            msg.put(target.getUUID(), player.getUUID());
        }
        return true;
    }

}
