package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.RandomUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class BroadcastCommand extends ICommand {

    public BroadcastCommand() {
        super("broadcast", "essentials.broadcast", "Send a chat message to all players", "/broadcast <message>", "bc");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.tf("&c/%s <message>", label));
            return;
        }
        final String message = RandomUtils.join(args, " ");
        sender.getServer().broadcastMessage(CC.t(message));
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.singletonList(CC.t(RandomUtils.join(args, " ")));
    }

}
