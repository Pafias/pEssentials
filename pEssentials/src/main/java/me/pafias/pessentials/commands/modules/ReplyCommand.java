package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.Messageable;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.services.UserManager;
import me.pafias.putils.CC;
import me.pafias.putils.Tasks;
import net.kyori.adventure.text.Component;
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
            if (commandSender instanceof Player player) {
                sender = plugin.getSM().getUserManager().getUser(player);
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
            final boolean colorize = sender.canColorize();

            if (!target.isBlockingPMsFrom(sender) || sender.canBypassBlock()) {
                final Component targetComponent = CC.a("&e[Tell] &c" + sender.getName() + "&6: ")
                        .append(colorize ? CC.a(message) : Component.text(message));
                target.message(targetComponent);
            }
            final Component senderComponent = CC.a("&e[Tell] &c" + sender.getName() + " &6-> &c" + target.getName() + "&6: ")
                    .append(colorize ? CC.a(message) : Component.text(message));
            sender.message(senderComponent);

            // SocialSpy
            Tasks.runAsync(() -> {
                final Component spyMessage = CC.a("&b&o[SocialSpy] &c&o" + sender.getName() + " &6&o-> &c&o" + target.getName() + "&6&o: ")
                        .append(colorize ? CC.a("&o" + message) : Component.text(message));
                final UserManager userManager = plugin.getSM().getUserManager();
                for (User spy : userManager.getUsers().values()) {
                    if (spy.isSpyingDms() && sender != spy && target != spy) {
                        spy.message(spyMessage);
                    }
                }
            });
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
