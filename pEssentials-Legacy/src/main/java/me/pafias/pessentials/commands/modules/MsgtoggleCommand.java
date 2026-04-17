package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.putils.LCC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MsgtoggleCommand extends ICommand {

    public MsgtoggleCommand() {
        super("msgtoggle", "essentials.msgtoggle", "Toggle your private messages", "/msgtoggle", "togglemsg");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LCC.t("&cOnly players!"));
            return;
        }
        final User user = plugin.getSM().getUserManager().getUser((Player) sender);
        user.setBlockingPMs(!user.isBlockingPMs());
        if (user.isBlockingPMs()) {
            sender.sendMessage(LCC.t("&cYou will no longer receive private messages."));
        } else {
            sender.sendMessage(LCC.t("&aYou can receive private messages again."));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
