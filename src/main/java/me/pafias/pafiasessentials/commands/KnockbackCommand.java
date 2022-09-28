package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class KnockbackCommand implements CommandExecutor {

    public static double x;
    public static double y;
    public static double z;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.knockback")) {
            if (args.length == 0) {
                sender.sendMessage(CC.translate(String.format("&c/%s <reset/x> [y] [z]", label)));
                return true;
            } else {
                if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
                    x = 1;
                    y = 1;
                    z = 1;
                    sender.sendMessage(CC.translate("&6Knockback reset to normal"));
                } else if (args.length == 3) {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                    sender.sendMessage(CC.translate("&aKnockback changed"));
                } else {
                    sender.sendMessage(CC.translate(String.format("&c/%s <reset/x> [y] [z]", label)));
                    return true;
                }
            }
        }
        return true;
    }

}
