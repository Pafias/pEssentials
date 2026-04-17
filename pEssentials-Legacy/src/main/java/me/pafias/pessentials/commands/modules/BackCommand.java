package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.putils.LCC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BackCommand extends ICommand {

    public BackCommand() {
        super("back", "essentials.back", "Go back", "/back");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LCC.t("&cOnly players!"));
            return;
        }
        final User player = plugin.getSM().getUserManager().getUser((Player) sender);
        if (player.lastLocation == null) {
            sender.sendMessage(LCC.t("&cNo last location available."));
            return;
        }
        player.getPlayer().teleport(player.lastLocation);
        sender.sendMessage(LCC.t("&6Teleported to last location."));
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
