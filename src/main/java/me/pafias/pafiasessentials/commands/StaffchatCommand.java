package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import me.pafias.pafiasessentials.util.RandomUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffchatCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public StaffchatCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.staffchat")) {
            if (args.length < 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players can toggle staffchat. You can chat in staffchat by putting a message after the command."));
                    return true;
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
                RandomUtils.getStaffOnline("essentials.staffchat").forEach(p -> p.sendMessage(CC.formatStaffchat(sender.getName(), message)));
                plugin.getServer().getConsoleSender().sendMessage(CC.formatStaffchat(sender.getName(), message));
            }
        }
        return true;
    }

}
