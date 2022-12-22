package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class GmspCommand extends ICommand {

    public GmspCommand() {
        super("gmsp", "essentials.gamemode", "Gamemode spectator", "/gmsp [player]", "gm3");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
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
                    return;
                }
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage(CC.t("&6Gamemode: &aSpectator"));
                sender.sendMessage(CC.t("&7" + target.getName() + "&6 " + (target.getName().endsWith("s") ? "'" : "'s") + "&6gamemode: &aSpectator"));
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
