package me.pafias.pessentials.commands.modules.Staff.Utility;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GmspCommand extends ICommand {

    public GmspCommand() {
        super("gmsp", "essentials.gamemode", "Gamemode spectator", "/gmsp [player]", "gm3");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(GamemodeCommand.gamemodePermissions.get(GameMode.SPECTATOR))) {
            sender.sendMessage(CC.t("&cYou do not have permission for this gamemode!"));
            return;
        }
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(CC.t("&6Gamemode: &aspectator"));
        } else {
            if (!sender.hasPermission(getPermission() + ".others")) {
                sender.sendMessage(CC.t("&cYou do not have permission to change other players' gamemodes!"));
                return;
            }
            boolean silent = Arrays.asList(args).contains("-s");
            if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*"))
                plugin.getServer().getOnlinePlayers().forEach(p -> {
                    p.setGameMode(GameMode.SPECTATOR);
                    if (!silent)
                        p.sendMessage(CC.t("&6Gamemode: &aspectator"));
                });
            else {
                final Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null || (sender instanceof Player senderPlayer && !senderPlayer.canSee(target))) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return;
                }
                target.setGameMode(GameMode.SPECTATOR);
                if (!silent)
                    target.sendMessage(CC.t("&6Gamemode: &aspectator"));
                sender.sendMessage(CC.tf("&6Gamemode for &7%s&6: &aspectator", target.getName()));
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(p -> ((Player) sender).canSee(p))
                .map(Player::getName)
                .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                .toList();
    }

}
