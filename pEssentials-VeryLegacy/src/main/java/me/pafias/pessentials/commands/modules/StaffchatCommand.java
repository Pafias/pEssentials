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
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players can toggle staffchat. You can chat in staffchat by putting a message after the command."));
                return;
            }
            final User player = plugin.getSM().getUserManager().getUser((Player) sender);
            player.setInStaffchat(!player.isInStaffchat());
            player.getPlayer().sendMessage(CC.t("&6Staffchat: " + (player.isInStaffchat() ? "&aON" : "&cOFF")));
        } else {
            final String message = RandomUtils.join(args, " ");
            for (Player p : RandomUtils.getStaffOnline("essentials.staffchat")) {
                p.sendMessage(CC.formatStaffchat(sender.getName(), message));
            }
            plugin.getServer().getConsoleSender().sendMessage(CC.formatStaffchat(sender.getName(), message));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
