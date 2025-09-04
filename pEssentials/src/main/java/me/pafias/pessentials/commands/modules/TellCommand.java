package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.Messageable;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.services.UserManager;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;

public class TellCommand extends ICommand {

    public TellCommand() {
        super("tell", null, "Private messaging", "/tell <player> <message>", "t", "whisper", "w", "message", "msg");
    }

    public final static Map<Messageable, Messageable> msg = new WeakHashMap<>();

    @Override
    public void commandHandler(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length < 2)
            commandSender.sendMessage(CC.t("&c/" + label + " <player> <message>"));
        else {
            final UserManager userManager = plugin.getSM().getUserManager();
            final Messageable sender;
            if (commandSender instanceof Player) {
                sender = userManager.getUser((Player) commandSender);
            } else {
                sender = userManager.getConsoleUser();
            }
            final Messageable target;
            if (args[0].equals(userManager.getConsoleUser().getName()) && commandSender.hasPermission("essentials.tell.console")) {
                target = userManager.getConsoleUser();
            } else {
                // Only get the user if you're either console, or if you can see them (accounting for vanish)
                Predicate<User> predicate = user -> (!(commandSender instanceof Player)) || ((Player) commandSender).canSee(user.getPlayer());
                target = userManager.getUser(args[0], predicate);
            }
            if (target == null) {
                commandSender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            if (target.isBlockingPMs()) {
                commandSender.sendMessage(CC.t("&cThat player has private messages turned off."));
                return;
            }
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            final String message = sb.toString();
            // TODO add a permission node for color codes
            if (!target.isBlockingPMsFrom(sender) || sender.canBypassBlock())
                target.message(true, "&e[Tell] &c" + sender.getName() + "&6: &r" + message);
            sender.message(true, "&e[Tell] &c" + sender.getName() + " &6-> &c" + target.getName() + " &6: &r" + message);

            msg.put(sender, target);
            if (!target.isBlockingPMsFrom(sender) || sender.canBypassBlock())
                msg.put(target, sender);
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        else {
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            final String message = sb.toString().trim();
            if (message.isEmpty())
                return Collections.emptyList();
            return Collections.singletonList(CC.t("&7Preview: &f" + message));
        }
    }

}
