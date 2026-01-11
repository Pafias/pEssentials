package me.pafias.pessentials.commands.modules.player.socials;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class BlockCommand extends ICommand {

    public BlockCommand() {
        super("block", "essentials.block", "Block (or unblock) someone to not see messages from them", "/block <player>", "ignore");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player senderPlayer)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        User user;
        if (args.length == 0) {
            user = plugin.getSM().getUserManager().getUser(senderPlayer);
            sender.sendMessage(CC.t("&6You are currently blocking:"));
            final StringBuilder sb = new StringBuilder();
            for (UUID blocked : user.getBlocking()) {
                final OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(blocked);
                sb.append("&c" + offlinePlayer.getName()).append("&f, ");
            }
            if (!sb.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
                sender.sendMessage(CC.t(sb.toString()));
            } else {
                sender.sendMessage(CC.t("&cNo one!"));
            }
        } else {
            final Player player = plugin.getServer().getPlayer(args[0]);
            if (player == null || !senderPlayer.canSee(player)) {
                sender.sendMessage(CC.t("&cPlayer not online!"));
                return;
            }
            user = plugin.getSM().getUserManager().getUser(senderPlayer);
            if (user.getBlocking().contains(player.getUniqueId())) {
                user.removeBlocking(player.getUniqueId());
                sender.sendMessage(CC.t("&aYou have unblocked &f" + player.getName()));
            } else {
                user.addBlocking(player.getUniqueId());
                sender.sendMessage(CC.t("&aYou have blocked &f" + player.getName()));
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return Collections.emptyList();
        List<String> list = new ArrayList<>();
        User user = plugin.getSM().getUserManager().getUser((Player) sender);
        list.addAll(user.getBlocking()
                .stream()
                .map(uuid -> plugin.getServer().getOfflinePlayer(uuid).getName())
                .filter(Objects::nonNull)
                .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                .toList()
        );
        list.addAll(plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(p -> ((Player) sender).canSee(p))
                .map(Player::getName)
                .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                .toList()
        );
        return list;
    }

}
