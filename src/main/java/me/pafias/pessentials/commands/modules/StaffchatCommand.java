package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.RandomUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class StaffchatCommand extends ICommand {

    public StaffchatCommand() {
        super("staffchat", "essentials.staffchat", "StaffChat", "/sc [message]", "sc");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.staffchat")) {
            if (args.length < 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players can toggle staffchat. You can chat in staffchat by putting a message after the command."));
                    return;
                }
                User player = plugin.getSM().getUserManager().getUser((Player) sender);
                player.setInStaffchat(!player.isInStaffChat());
                player.getPlayer().sendMessage(CC.t("&6Staffchat: " + (player.isInStaffChat() ? "&aON" : "&cOFF")));
            } else {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String message = sb.toString();
                try {
                    RandomUtils.getStaffOnline("essentials.staffchat").forEach(p -> p.sendMessage(CC.formatStaffchatModern(sender.getName(), message)));
                    plugin.getServer().getConsoleSender().sendMessage(CC.formatStaffchatModern(sender.getName(), message));
                } catch (Exception ex) {
                    RandomUtils.getStaffOnline("essentials.staffchat").forEach(p -> p.sendMessage(CC.formatStaffchat(sender.getName(), message)));
                    plugin.getServer().getConsoleSender().sendMessage(CC.formatStaffchat(sender.getName(), message));
                }
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
