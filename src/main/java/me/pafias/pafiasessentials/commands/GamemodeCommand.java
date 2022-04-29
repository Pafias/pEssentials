package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public GamemodeCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.gamemode")) {
            if (args.length == 0) {
                sender.sendMessage(CC.translate("&c/" + label + " <gamemode> [player]"));
                return true;
            } else if (args.length == 1) {
                GameMode gamemode = null;
                try {
                    gamemode = GameMode.getByValue(Integer.parseInt(args[0]));
                } catch (NumberFormatException ex) {
                    try {
                        for (GameMode gm : GameMode.values())
                            if (gm.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                                gamemode = gm;
                                break;
                            }
                    } catch (IllegalArgumentException exx) {
                        sender.sendMessage(CC.translate("&cInvalid gamemode!"));
                        return true;
                    }
                }
                if (gamemode == null) {
                    sender.sendMessage(CC.translate("&cInvalid gamemode!"));
                    return true;
                }
                ((Player) sender).setGameMode(gamemode);
                sender.sendMessage(CC.translate("&6Gamemode: &a" + gamemode.name().toLowerCase()));
            } else {
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(CC.translate("&cPlayer not found!"));
                    return true;
                }
                GameMode gamemode = null;
                try {
                    gamemode = GameMode.getByValue(Integer.parseInt(args[0]));
                } catch (NumberFormatException ex) {
                    try {
                        for (GameMode gm : GameMode.values())
                            if (gm.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                                gamemode = gm;
                                break;
                            }
                    } catch (IllegalArgumentException exx) {
                        sender.sendMessage(CC.translate("&cInvalid gamemode!"));
                        return true;
                    }
                }
                if (gamemode == null) {
                    sender.sendMessage(CC.translate("&cInvalid gamemode!"));
                    return true;
                }
                target.setGameMode(gamemode);
                target.sendMessage(CC.translate("&6Gamemode: &a" + gamemode.name().toLowerCase()));
                sender.sendMessage(CC.translate("&7" + target.getName() + "&6 " + (target.getName().endsWith("s") ? "'" : "'s") + "&6gamemode: &a" + gamemode.name().toLowerCase()));
            }
        }
        return true;
    }

}
