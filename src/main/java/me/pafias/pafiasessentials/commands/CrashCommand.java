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
                sender.sendMessage(CC.translate("&c/crash <player> [amount of times (use higher number here if they have a good pc)]"));
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.translate("&cPlayer not found!"));
                return true;
            }
            int times;
            if (args.length > 1) {
                try {
                    times = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.translate("&cInvalid number"));
                    return true;
                }
            } else times = 1;
            crash(target, times);
            sender.sendMessage(CC.translate("&aTarget crashed."));
            return true;
        }
        return true;
    }

    private void crash(Player player, int times) {
        for (int i = 0; i < times; i++)
            plugin.getSM().getNMSProvider().sendParticle(player, "CRIT", player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ(), Integer.MAX_VALUE);
    }

}
