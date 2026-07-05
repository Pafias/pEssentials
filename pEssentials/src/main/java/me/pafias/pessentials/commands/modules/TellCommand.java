package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.Messageable;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.services.UserManager;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.CC;
import me.pafias.putils.Tasks;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TellCommand extends ICommand {

    public TellCommand() {
        super("tell", null, "Private messaging", "/tell <player> <message>", "t", "whisper", "w", "message", "msg");
        privateMessagingPreview = getPlugin().getConfig().getBoolean("private_messaging_preview", false);
    }

    private final boolean privateMessagingPreview;

    public final static Map<Messageable, Messageable> msg = new HashMap<>();

    @Override
    public void commandHandler(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length < 2)
            commandSender.sendMessage(CC.t("&c/" + label + " <player> <message>"));
        else {
            final UserManager userManager = plugin.getSM().getUserManager();
            final Messageable sender;
            if (commandSender instanceof Player player) {
                sender = userManager.getUser(player);
            } else {
                sender = userManager.getConsoleUser();
            }
            final Messageable target;
            if (args[0].equals(userManager.getConsoleUser().getName()) && commandSender.hasPermission("essentials.tell.console")) {
                target = userManager.getConsoleUser();
            } else {
                // Only get the user if you're either console, or if you can see them (accounting for vanish)
                Predicate<User> predicate = user -> (!(commandSender instanceof Player player)) || player.canSee(user.getPlayer());
                target = userManager.getUser(args[0], predicate);
            }
            if (target == null) {
                commandSender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            if (target.isBlockingPMs() && !sender.canBypassMsgtoggle()) {
                commandSender.sendMessage(CC.t("&cThat player has private messages turned off."));
                return;
            }
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            final String message = sb.toString();

            boolean colorize = sender.canColorize();
            boolean notBlocked = !target.isBlockingPMsFrom(sender) || sender.canBypassBlock();

            if (notBlocked) {
                final Component targetMessageComponent = CC.a("&e[Tell] &c" + sender.getName() + "&6: ")
                        .append(colorize ? CC.a(message) : Component.text(message));
                target.message(targetMessageComponent);
            }
            final Component senderMessageComponent = CC.a("&e[Tell] &c" + sender.getName() + " &6-> &c" + target.getName() + "&6: ")
                    .append(colorize ? CC.a(message) : Component.text(message));
            sender.message(senderMessageComponent);

            msg.put(sender, target);
            if (notBlocked)
                msg.put(target, sender);

            // SocialSpy
            Tasks.runAsync(() -> {
                final Component spyMessage = CC.a("&b&o[SocialSpy] &c&o" + sender.getName() + " &6&o-> &c&o" + target.getName() + "&6&o: ")
                        .append(colorize ? CC.a("&o" + message) : Component.text(message));
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
        if (args.length == 1) {
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        } else {
            if (privateMessagingPreview) {
                final StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++)
                    sb.append(args[i]).append(" ");
                final String message = sb.toString().trim();
                if (message.isEmpty())
                    return Collections.emptyList();
                return Collections.singletonList(CC.t("&7Preview: &f" + message));
            } else
                return Collections.emptyList();
        }
    }

}
