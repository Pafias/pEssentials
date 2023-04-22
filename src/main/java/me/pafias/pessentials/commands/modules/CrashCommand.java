package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CrashCommand extends ICommand {

    public CrashCommand() {
        super("crash", "essentials.crash", "Crash someone's client", "/crash <player>");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length < 1) {
                sender.sendMessage(CC.t("&c/crash <player> [amount]"));
                sender.sendMessage(CC.t("&6amount = the amount of times to execute. (default = 1). use a higher number if they have a good pc"));
                return;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            if (target.getUniqueId().toString().equals("a89e7e16-eed4-4dbc-99b1-7702f8060cda")) {
                if (sender instanceof Player)
                    target = (Player) sender;
                else {
                    sender.sendMessage(CC.t("&cNo."));
                    return;
                }
            }
            int times;
            if (args.length > 1) {
                try {
                    times = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid number"));
                    return;
                }
            } else times = 1;
            crash(target, times);
            sender.sendMessage(CC.t("&aTarget crashed."));
            return;
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream().filter(p -> ((Player) sender).canSee(p)).map(Player::getName).filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        else return Collections.emptyList();
    }

    private void crash(Player player, int times) {
        User target = plugin.getSM().getUserManager().getUser(player);
        for (int i = 0; i < times; i++)
            target.crash();
    }

}
