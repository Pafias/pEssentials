package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GamemodeCommand extends ICommand {

    public GamemodeCommand() {
        super("gamemode", "essentials.gamemode", "Gamemode", "/gamemode <gamemode> [player]", "gm");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.gamemode")) {
            if (args.length == 0) {
                sender.sendMessage(CC.t("&c/" + label + " <gamemode> [player]"));
                return;
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
                        sender.sendMessage(CC.t("&cInvalid gamemode!"));
                        return;
                    }
                }
                if (gamemode == null) {
                    sender.sendMessage(CC.t("&cInvalid gamemode!"));
                    return;
                }
                ((Player) sender).setGameMode(gamemode);
                sender.sendMessage(CC.t("&6Gamemode: &a" + gamemode.name().toLowerCase()));
            } else {
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
                        sender.sendMessage(CC.t("&cInvalid gamemode!"));
                        return;
                    }
                }
                if (gamemode == null) {
                    sender.sendMessage(CC.t("&cInvalid gamemode!"));
                    return;
                }
                boolean silent = Arrays.asList(args).contains("-s");
                if (args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("*")) {
                    GameMode finalGamemode = gamemode;
                    plugin.getServer().getOnlinePlayers().forEach(p -> {
                        p.setGameMode(finalGamemode);
                        if (!silent)
                            p.sendMessage(CC.t("&6Gamemode: &a" + finalGamemode.name().toLowerCase()));
                    });
                } else {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(CC.t("&cPlayer not found!"));
                        return;
                    }
                    target.setGameMode(gamemode);
                    if (!silent)
                        target.sendMessage(CC.t("&6Gamemode: &a" + gamemode.name().toLowerCase()));
                    sender.sendMessage(CC.t("&7" + target.getName() + "&6 " + (target.getName().endsWith("s") ? "'" : "'s") + "&6gamemode: &a" + gamemode.name().toLowerCase()));
                }
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return Arrays.asList("survival", "creative", "adventure", "spectator", "0", "1", "2", "3");
        else if (args.length == 2)
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
