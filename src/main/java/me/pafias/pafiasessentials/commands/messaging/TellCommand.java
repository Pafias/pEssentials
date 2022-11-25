package me.pafias.pafiasessentials.commands.messaging;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class TellCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public TellCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    public static Map<UUID, UUID> msg = new WeakHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.t("&c/" + label + " <player> <message>"));
            return true;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return true;
            }
            User player = plugin.getSM().getUserManager().getUser((Player) sender);
            User target = plugin.getSM().getUserManager().getUser(args[0]);
            if (target == null || target.isVanished()) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            String message = sb.toString();
            target.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + "&6: &r" + message));
            player.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));
            msg.put(player.getUUID(), target.getUUID());
            msg.put(target.getUUID(), player.getUUID());
        }
        return true;
    }

}
