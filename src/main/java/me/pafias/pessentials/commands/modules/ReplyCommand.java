package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReplyCommand extends ICommand {

    public ReplyCommand() {
        super("reply", null, "Reply", "/r <message>", "r");
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
            if (target.isBlockingPMs() && !player.getPlayer().hasPermission("essentials.msgtoggle.bypass")) {
                sender.sendMessage(CC.t("&cThat player has private messages turned off."));
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String arg : args) sb.append(arg).append(" ");
            String message = sb.toString();
            try {
                target.getPlayer().sendMessage(CC.a("&e[Tell] &c" + player.getName() + "&6: &r" + message));
                player.getPlayer().sendMessage(CC.a("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));
            } catch (Throwable ex) {
                target.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + "&6: &r" + message));
                player.getPlayer().sendMessage(CC.t("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
