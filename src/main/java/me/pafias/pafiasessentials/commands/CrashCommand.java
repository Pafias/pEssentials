package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrashCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public CrashCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length < 1) {
                sender.sendMessage(CC.t("&c/crash <player> [amount]"));
                sender.sendMessage(CC.t("&6amount = the amount of times to execute. (default = 1). use a higher number if they have a good pc"));
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return true;
            }
            int times;
            if (args.length > 1) {
                try {
                    times = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid number"));
                    return true;
                }
            } else times = 1;
            crash(target, times);
            sender.sendMessage(CC.t("&aTarget crashed."));
            return true;
        }
        return true;
    }

    private void crash(Player player, int times) {
        for (int i = 0; i < times; i++)
            plugin.getSM().getNMSProvider().crash(player);
    }

}
