package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        player.performCommand("give " + player.getName() + " " + sb.toString());
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> materials = new java.util.ArrayList<>();
            for (Material m : Material.values()) {
                String name = m.name().toLowerCase();
                int id = m.getId();
                if (name.toLowerCase().startsWith(args[0].toLowerCase()) || String.valueOf(id).startsWith(args[0]))
                    materials.add(name);
            }
            return materials;
        } else {
            return Collections.emptyList();
        }
    }

}
