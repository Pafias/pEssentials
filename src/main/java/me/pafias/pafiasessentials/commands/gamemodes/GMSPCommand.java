package me.pafias.pafiasessentials.commands.gamemodes;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GMSPCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public GMSPCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return true;
        }
        if (args.length == 0) {
            if (sender.hasPermission("essentials.gamemode")) {
                Player player = (Player) sender;
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(CC.t("&6Gamemode: &aSpectator"));
            }
        } else {
            if (sender.hasPermission("essentials.gamemode.others")) {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return true;
                }
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage(CC.t("&6Gamemode: &aSpectator"));
                sender.sendMessage(CC.t("&7" + target.getName() + "&6 " + (target.getName().endsWith("s") ? "'" : "'s") + "&6gamemode: &aSpectator"));
            }
        }
        return true;
    }

}
