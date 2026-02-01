package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.putils.builders.TexturedSkullBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class TexturedskullCommand extends ICommand {

    public TexturedskullCommand() {
        super("texturedskull", "essentials.skull", "Get a skull with a base64 texture string or a skin url", "/texturedskull <base64 string>", "texturedplayerhead");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.t("&c/" + label + " <base64 texture string OR skin url>"));
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        TexturedSkullBuilder builder;
        if (args[0].toLowerCase().startsWith("http")) {
            try {
                builder = new TexturedSkullBuilder(new URL(args[0]));
            } catch (MalformedURLException e) {
                sender.sendMessage(CC.t("&cInvalid URL."));
                return;
            }
        } else {
            builder = new TexturedSkullBuilder(args[0]);
        }
        builder.buildAsync().thenAccept(skull -> {
            if (skull != null) {
                player.getInventory().addItem(skull);
                player.sendMessage(CC.t("&6Received skull"));
            } else {
                player.sendMessage(CC.t("&cFailed to create skull."));
            }
        });
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
