package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SudoCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public SudoCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if (args.length < 2) {
                sender.sendMessage(CC.t(String.format("&c/%s <player> <command>", label)));
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return true;
            }
            PluginCommand cmd = plugin.getServer().getPluginCommand(args[1]);
            if (cmd == null) {
                sender.sendMessage(CC.t("&cCommand not found!"));
                return true;
            }
            String[] argsParsed = Arrays.copyOfRange(args, 2, args.length);
            PermissionAttachment attachment = target.addAttachment(plugin);
            boolean hasPerm = cmd.testPermissionSilent(target);
            boolean removePerm;
            if (!hasPerm && cmd.getPermission() != null) {
                attachment.setPermission(cmd.getPermission(), true);
                removePerm = true;
            } else removePerm = false;
            cmd.execute(target, args[1], argsParsed);
            if (removePerm)
                attachment.unsetPermission(cmd.getPermission());
            target.removeAttachment(attachment);
            sender.sendMessage(CC.t("&aSudo executed."));
        }
        return true;
    }

}
