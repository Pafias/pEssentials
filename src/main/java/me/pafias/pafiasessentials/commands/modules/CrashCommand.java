package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
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
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        else return Collections.emptyList();
    }

    private void crash(Player player, int times) {
        User target = plugin.getSM().getUserManager().getUser(player);
        for (int i = 0; i < times; i++)
            target.crash();
    }

}
