package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.events.PlayerLaunchedEvent;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class LaunchCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public LaunchCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.launch")) {
            if (args.length == 0) {
                sender.sendMessage(CC.tf("&c/%s <player>", label));
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return true;
            }
            double height = 1.5;
            if (args.length >= 2)
                try {
                    height = Double.parseDouble(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid number."));
                    return true;
                }
            target.setVelocity(target.getVelocity().add(new Vector(0, height, 0)));
            plugin.getServer().getPluginManager().callEvent(new PlayerLaunchedEvent(sender, target, height));
            sender.sendMessage(CC.tf("&aLaunched &d%s", target.getName()));
        }
        return true;
    }

}
