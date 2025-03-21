package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TellCommand extends ICommand {

    public TellCommand() {
        super("tell", null, "Private messaging", "/tell <player> <message>", "t", "whisper", "w", "message", "msg");
    }

    public static Map<UUID, UUID> msg = new WeakHashMap<>();

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.t("&c/" + label + " <player> <message>"));
            return;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            User player = plugin.getSM().getUserManager().getUser((Player) sender);
            User target = plugin.getSM().getUserManager().getUser(args[0]);
            if (target == null || target.isVanished()) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            if (target.isBlockingPMs() && !player.getPlayer().hasPermission("essentials.msgtoggle.bypass")) {
                sender.sendMessage(CC.t("&cThat player has private messages turned off."));
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            String message = sb.toString();
            try {
                if (!target.getBlocking().contains(player.getUUID()) || player.getPlayer().hasPermission("essentials.block.bypass"))
                    target.getPlayer().sendMessage(CC.a("&e[Tell] &c" + player.getName() + "&6: &r" + message));
                player.getPlayer().sendMessage(CC.a("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));
            } catch (Throwable ex) {
                if (!target.getBlocking().contains(player.getUUID()) || player.getPlayer().hasPermission("essentials.block.bypass"))
                    target.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + "&6: &r" + message));
                player.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));
            }

            msg.put(player.getUUID(), target.getUUID());
            if (!target.getBlocking().contains(player.getUUID()) || player.getPlayer().hasPermission("essentials.block.bypass"))
                msg.put(target.getUUID(), player.getUUID());
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
