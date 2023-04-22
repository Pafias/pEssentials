package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class KnockbackCommand extends ICommand {

    public static double x = 1;
    public static double y = 1;
    public static double z = 1;

    public KnockbackCommand() {
        super("knockback", "essentials.knockback", "Modify the knockback", "/kb <reset/x> [y] [z]", "kb");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.knockback")) {
            if (args.length == 0) {
                sender.sendMessage(CC.t(String.format("&c/%s <reset/x> [y] [z]", label)));
            } else {
                if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
                    x = 1;
                    y = 1;
                    z = 1;
                    sender.sendMessage(CC.t("&6Knockback reset to normal"));
                } else if (args.length == 3) {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                    sender.sendMessage(CC.t("&aKnockback changed"));
                } else {
                    sender.sendMessage(CC.t(String.format("&c/%s <reset/x> [y] [z]", label)));
                }
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
