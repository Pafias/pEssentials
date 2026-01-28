package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.Messageable;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReplyCommand extends ICommand {

    public ReplyCommand() {
        super("reply", null, "Reply", "/r <message>", "r");
        privateMessagingPreview = getPlugin().getConfig().getBoolean("private_messaging_preview", false);
    }

    private final boolean privateMessagingPreview;

    @Override
    public void commandHandler(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(CC.t("&c/" + label + " <message>"));
        } else {
            final Messageable sender;
            if (commandSender instanceof Player) {
                sender = plugin.getSM().getUserManager().getUser((Player) commandSender);
            } else {
                sender = plugin.getSM().getUserManager().getConsoleUser();
            }
            if (!TellCommand.msg.containsKey(sender)) {
                commandSender.sendMessage(CC.t("&cYou haven't messaged anybody recently!"));
                return;
            }
            final Messageable target = TellCommand.msg.get(sender);
            if (target == null || !target.isOnline()) {
                commandSender.sendMessage(CC.t("&cThe person you were chatting with is no longer online!"));
                return;
            }
            if (target.isBlockingPMs() && !sender.canBypassMsgtoggle()) {
                commandSender.sendMessage(CC.t("&cThat player has private messages turned off."));
                return;
            }
            final StringBuilder sb = new StringBuilder();
            for (String arg : args) sb.append(arg).append(" ");
            final String message = sb.toString();
            if (!target.isBlockingPMsFrom(sender) || sender.canBypassBlock())
                target.message(true, "&e[Tell] &c" + sender.getName() + "&6: &r" + message);
            sender.message(true, "&e[Tell] &c" + sender.getName() + " &6-> &c" + target.getName() + " &6: &r" + message);
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (privateMessagingPreview) {
            final String message = String.join(" ", args).trim();
            if (message.isEmpty())
                return Collections.emptyList();
            return Collections.singletonList(CC.t("&7Preview: &f" + message));
        } else
            return Collections.emptyList();
    }

}
