package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

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
        } else {
            GameMode gamemode = parseGamemode(args[0]);
            if (gamemode == null) {
                sender.sendMessage(CC.t("&cInvalid gamemode!"));
                return;
            }
            if (!sender.hasPermission(gamemodePermissions.get(gamemode))) {
                sender.sendMessage(CC.t("&cYou do not have permission for this gamemode!"));
                return;
            }
            if (args.length == 1) {
                ((Player) sender).setGameMode(gamemode);
                sender.sendMessage(CC.tf("&6Gamemode: &a%s", gamemode.name().toLowerCase()));
            } else {
                if (!sender.hasPermission("essentials.gamemode.others")) {
                    sender.sendMessage(CC.t("&cYou do not have permission to change other players' gamemodes!"));
                    return;
                }
                final boolean silent = Arrays.asList(args).contains("-s");
                if (args[1].equalsIgnoreCase("@a") || args[1].equalsIgnoreCase("*")) {
                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                        p.setGameMode(gamemode);
                        if (!silent)
                            p.sendMessage(CC.t("&6Gamemode: &a" + gamemode.name().toLowerCase()));
                    }
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
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            for (GameMode gamemode : GameMode.values()) {
                if (sender.hasPermission(gamemodePermissions.get(gamemode))) {
                    String name = gamemode.name().toLowerCase();
                    if (name.startsWith(args[0].toLowerCase()))
                        list.add(name);
                }
            }
            return list;
        } else if (args.length == 2 && sender.hasPermission("essentials.gamemode.others")) {
            List<String> list = new ArrayList<>();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String name = p.getName();
                    if (name.toLowerCase().startsWith(args[1].toLowerCase()))
                        list.add(name);
                }
            }
            return list;
        } else {
            return Collections.emptyList();
        }
    }

}
