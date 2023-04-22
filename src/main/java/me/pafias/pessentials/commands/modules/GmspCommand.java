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
                boolean silent = Arrays.asList(args).contains("-s");
                if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*"))
                    plugin.getServer().getOnlinePlayers().forEach(p -> {
                        p.setGameMode(GameMode.SPECTATOR);
                        if (!silent)
                            p.sendMessage(CC.t("&6Gamemode: &aSpectator"));
                    });
                else {
                    Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(CC.t("&cPlayer not found!"));
                        return;
                    }
                    target.setGameMode(GameMode.SPECTATOR);
                    if (!silent)
                        target.sendMessage(CC.t("&6Gamemode: &aSpectator"));
                    sender.sendMessage(CC.t("&7" + target.getName() + "&6 " + (target.getName().endsWith("s") ? "'" : "'s") + "&6gamemode: &aSpectator"));
                }
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return plugin.getServer().getOnlinePlayers().stream().filter(p -> ((Player) sender).canSee(p)).map(Player::getName).filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
    }

}
