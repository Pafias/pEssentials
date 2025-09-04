package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TellCommand extends ICommand {

    public TellCommand() {
        super("tell", null, "Private messaging", "/tell <player> <message>", "t", "whisper", "w", "message", "msg");
    }

    public final static Map<UUID, UUID> msg = new WeakHashMap<>();

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2)
            sender.sendMessage(CC.t("&c/" + label + " <player> <message>"));
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            final User player = plugin.getSM().getUserManager().getUser((Player) sender);
            final User target = plugin.getSM().getUserManager().getUser(args[0]);
            if (target == null || target.isVanished()) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            if (target.isBlockingPMs() && !player.getPlayer().hasPermission("essentials.msgtoggle.bypass")) {
                sender.sendMessage(CC.t("&cThat player has private messages turned off."));
                return;
            }
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            final String message = sb.toString();
            if (!target.getBlocking().contains(player.getName()) || player.getPlayer().hasPermission("essentials.block.bypass"))
                target.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + "&6: &r" + message));
            player.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));

            msg.put(player.getUUID(), target.getUUID());
            if (!target.getBlocking().contains(player.getName()) || player.getPlayer().hasPermission("essentials.block.bypass"))
                msg.put(target.getUUID(), player.getUUID());
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
