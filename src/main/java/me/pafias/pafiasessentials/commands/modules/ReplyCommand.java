package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReplyCommand extends ICommand {

    public ReplyCommand() {
        super("reply", "essentials.reply", "Reply", "/r <message>", "r");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.t("&c/" + label + " <message>"));
            return;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            User player = plugin.getSM().getUserManager().getUser((Player) sender);
            if (!TellCommand.msg.containsKey(player.getUUID())) {
                player.getPlayer().sendMessage(CC.t("&cYou haven't messaged anybody recently!"));
                return;
            }
            User target = plugin.getSM().getUserManager().getUser(TellCommand.msg.get(player.getUUID()));
            if (target == null || target.isVanished()) {
                sender.sendMessage(CC.t("&cThe person you were chatting with is no longer online!"));
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String arg : args) sb.append(arg).append(" ");
            String message = sb.toString();
            target.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + "&6: &r" + message));
            player.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
