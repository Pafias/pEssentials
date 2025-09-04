package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TopCommand extends ICommand {

    public TopCommand() {
        super("top", "essentials.top", "Go to the top", "/top");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        final Block highest = player.getLocation().getWorld().getHighestBlockAt(player.getLocation());
        if (highest == null || highest.getType().equals(Material.AIR)) {
            player.sendMessage(CC.t("&cNo highest block found."));
            return;
        }
        player.teleport(highest.getLocation());
        player.sendMessage(CC.t("&6Teleported to the highest block!"));
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
