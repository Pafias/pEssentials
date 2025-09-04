package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockCommand extends ICommand {

    public BlockCommand() {
        super("block", "essentials.block", "Block (or unblock) someone to not see messages from them", "/block <player>", "ignore");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        User user;
        if (args.length == 0) {
            user = plugin.getSM().getUserManager().getUser((Player) sender);
            sender.sendMessage(CC.t("&6You are currently blocking:"));
            final StringBuilder sb = new StringBuilder();
            for (String blocked : user.getBlocking()) {
                final OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(blocked);
                sb.append("&c" + offlinePlayer.getName()).append("&f, ");
            }
            if (sb.length() > 0) {
                sb.delete(sb.length() - 2, sb.length());
                sender.sendMessage(CC.t(sb.toString()));
            } else {
                sender.sendMessage(CC.t("&cNo one!"));
            }
        } else {
            final Player player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(CC.t("&cPlayer not online!"));
                return;
            }
            user = plugin.getSM().getUserManager().getUser((Player) sender);
            if (user.getBlocking().contains(player.getName())) {
                user.getBlocking().remove(player.getName());
                sender.sendMessage(CC.t("&aYou have unblocked &f" + player.getName()));
            } else {
                user.getBlocking().add(player.getName());
                sender.sendMessage(CC.t("&aYou have blocked &f" + player.getName()));
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return Collections.emptyList();
        List<String> list = new ArrayList<String>();
        User user = plugin.getSM().getUserManager().getUser((Player) sender);
        String prefix = args[0].toLowerCase();
        for (String name : user.getBlocking()) {
            OfflinePlayer player = plugin.getServer().getOfflinePlayer(name);
            if (player != null && player.getName().toLowerCase().startsWith(prefix))
                list.add(name);
        }
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (((Player) sender).canSee(p)) {
                String name = p.getName();
                if (name != null && name.toLowerCase().startsWith(prefix))
                    list.add(name);
            }
        }
        return list;
    }

}
