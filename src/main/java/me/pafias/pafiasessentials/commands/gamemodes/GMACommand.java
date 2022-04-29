package me.pafias.pafiasessentials.commands.gamemodes;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GMACommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public GMACommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cOnly players!"));
            return true;
        }
        if (args.length == 0) {
            if (sender.hasPermission("essentials.gamemode")) {
                Player player = (Player) sender;
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(CC.translate("&6Gamemode: &aAdventure"));
            }
        } else {
            if (sender.hasPermission("essentials.gamemode.others")) {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found!"));
                    return true;
                }
                target.setGameMode(GameMode.ADVENTURE);
                target.sendMessage(CC.translate("&6Gamemode: &aAdventure"));
                sender.sendMessage(CC.translate("&7" + target.getName() + "&6 " + (target.getName().endsWith("s") ? "'" : "'s") + "&6gamemode: &aAdventure"));
            }
        }
        return true;
    }

}
