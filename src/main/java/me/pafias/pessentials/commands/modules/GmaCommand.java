package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GmaCommand extends ICommand {

    public GmaCommand() {
        super("gma", "essentials.gamemode", "Gamemode adventure", "/gma [player]", "gm2");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        if (!sender.hasPermission(GamemodeCommand.gamemodePermissions.get(GameMode.ADVENTURE))) {
            sender.sendMessage(CC.t("&cYou do not have permission for this gamemode!"));
            return;
        }
        if (args.length == 0) {
            Player player = (Player) sender;
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(CC.t("&6Gamemode: &aadventure"));
        } else {
            if (!sender.hasPermission("essentials.gamemode.others")) {
                sender.sendMessage(CC.t("&cYou do not have permission to change other players' gamemodes!"));
                return;
            }
            boolean silent = Arrays.asList(args).contains("-s");
            if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*"))
                plugin.getServer().getOnlinePlayers().forEach(p -> {
                    p.setGameMode(GameMode.ADVENTURE);
                    if (!silent)
                        p.sendMessage(CC.t("&6Gamemode: &aadventure"));
                });
            else {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return;
                }
                target.setGameMode(GameMode.ADVENTURE);
                if (!silent)
                    target.sendMessage(CC.t("&6Gamemode: &aadventure"));
                sender.sendMessage(CC.tf("&6Gamemode for &7%s&6: &aadventure", target.getName()));
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return plugin.getServer().getOnlinePlayers().stream().filter(p -> ((Player) sender).canSee(p)).map(Player::getName).filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
    }

}
