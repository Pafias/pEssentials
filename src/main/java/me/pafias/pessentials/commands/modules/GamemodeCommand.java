package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GamemodeCommand extends ICommand {

    public GamemodeCommand() {
        super("gamemode", "essentials.gamemode", "Gamemode", "/gamemode <gamemode> [player]", "gm");
    }

    public final static Map<GameMode, String> gamemodePermissions = new HashMap<>();

    static {
        for (GameMode gameMode : GameMode.values())
            gamemodePermissions.put(gameMode, "essentials.gamemode." + gameMode.name().toLowerCase());
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
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
            if (!sender.hasPermission(gamemodePermissions.get(gamemode))) {
                sender.sendMessage(CC.t("&cYou do not have permission for this gamemode!"));
                return;
            }
            ((Player) sender).setGameMode(gamemode);
            sender.sendMessage(CC.tf("&6Gamemode: &a%s", gamemode.name().toLowerCase()));
        } else {
            if (!sender.hasPermission("essentials.gamemode.others")) {
                sender.sendMessage(CC.t("&cYou do not have permission to change other players' gamemodes!"));
                return;
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
                    sender.sendMessage(CC.t("&cInvalid gamemode!"));
                    return;
                }
            }
            if (gamemode == null) {
                sender.sendMessage(CC.t("&cInvalid gamemode!"));
                return;
            }
            if (!sender.hasPermission(gamemodePermissions.get(gamemode))) {
                sender.sendMessage(CC.t("&cYou do not have permission for this gamemode!"));
                return;
            }
            final boolean silent = Arrays.asList(args).contains("-s");
            if (args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("*")) {
                final GameMode finalGamemode = gamemode;
                plugin.getServer().getOnlinePlayers().forEach(p -> {
                    p.setGameMode(finalGamemode);
                    if (!silent)
                        p.sendMessage(CC.t("&6Gamemode: &a" + finalGamemode.name().toLowerCase()));
                });
            } else {
                final Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return;
                }
                target.setGameMode(gamemode);
                if (!silent)
                    target.sendMessage(CC.tf("&6Gamemode: &a%s", gamemode.name().toLowerCase()));
                sender.sendMessage(CC.tf("&6Gamemode for &7%s&6: &a%s", target.getName(), gamemode.name().toLowerCase()));
            }
        }
    }

    private GameMode parseGamemode(String input) {
        if (input == null || input.isEmpty()) return null;
        try {
            // Try parsing by number
            return GameMode.getByValue(Integer.parseInt(input));
        } catch (NumberFormatException ex) {
            // Then try parsing by name
            for (GameMode gm : GameMode.values())
                if (gm.name().startsWith(input.toUpperCase()))
                    return gm;
        }
        return null;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return Stream.of(GameMode.values())
                    .filter(gamemode -> sender.hasPermission(gamemodePermissions.get(gamemode)))
                    .map(gamemode -> gamemode.name().toLowerCase())
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        else if (args.length == 2 && sender.hasPermission("essentials.gamemode.others"))
            return plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
