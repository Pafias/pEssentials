package me.pafias.pessentials.commands.modules.Staff.Utility;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemCommand extends ICommand {

    public ItemCommand() {
        super("item", "essentials.item", "Get an item", "/item <item>", "i", "giveitem");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        final StringBuilder sb = new StringBuilder();
        for (String arg : args)
            sb.append(arg).append(" ");
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        player.performCommand("give " + player.getName() + " " + sb.toString());
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return Arrays.stream(Material.values())
                    .map(m -> {
                        try {
                            return m.key().asMinimalString();
                        } catch (Throwable t) {
                            return m.key().value();
                        }
                    })
                    .filter(m -> {
                        if (m.toLowerCase().startsWith(args[0].toLowerCase())) return true;
                        String[] split = m.split(":");
                        return split.length > 1 && split[1].toLowerCase().startsWith(args[0].toLowerCase());
                    })
                    .toList();
        else return Collections.emptyList();
    }

}
