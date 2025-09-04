package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.RandomUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SudoCommand extends ICommand {

    public SudoCommand() {
        super("sudo", "essentials.sudo", "Sudo someone", "/sudo <player> <command>");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if (args.length < 2) {
                sender.sendMessage(CC.t(String.format("&c/%s <player> <command>", label)));
                return;
            }
            final Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            final String[] argsParsed = Arrays.copyOfRange(args, 2, args.length);
            if (args[1].equalsIgnoreCase("chat")) {
                final String message = RandomUtils.join(argsParsed, " ");
                target.chat(message);
            } else {
                final PluginCommand cmd = plugin.getServer().getPluginCommand(args[1]);
                if (cmd == null) {
                    sender.sendMessage(CC.t("&cCommand not found!"));
                    return;
                }
                final PermissionAttachment attachment = target.addAttachment(plugin);
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
            }
            sender.sendMessage(CC.t("&aSudo executed."));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> names = new java.util.ArrayList<>();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String name = p.getName();
                    if (name != null && name.toLowerCase().startsWith(args[0].toLowerCase()))
                        names.add(name);
                }
            }
            return names;
        } else if (args.length == 2) {
            List<String> cmds = new java.util.ArrayList<>();
            for (String cmdName : plugin.getServer().getCommandAliases().keySet()) {
                PluginCommand cmd = plugin.getServer().getPluginCommand(cmdName);
                if (cmd != null && (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission()))) {
                    String name = cmd.getName();
                    if (name != null && name.toLowerCase().startsWith(args[1].toLowerCase()))
                        cmds.add(name);
                }
            }
            return cmds;
        } else {
            return Collections.singletonList("<args>");
        }
    }

}
