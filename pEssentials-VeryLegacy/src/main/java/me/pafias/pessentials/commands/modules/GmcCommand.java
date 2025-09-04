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

public class GmcCommand extends ICommand {

    public GmcCommand() {
        super("gmc", "essentials.gamemode", "Gamemode creative", "/gmc [player]", "gm1");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        if (!sender.hasPermission(GamemodeCommand.gamemodePermissions.get(GameMode.CREATIVE))) {
            sender.sendMessage(CC.t("&cYou do not have permission for this gamemode!"));
            return;
        }
        if (args.length == 0) {
            final Player player = (Player) sender;
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(CC.t("&6Gamemode: &acreative"));
        } else {
            if (!sender.hasPermission("essentials.gamemode.others")) {
                sender.sendMessage(CC.t("&cYou do not have permission to change other players' gamemodes!"));
                return;
            }
            boolean silent = Arrays.asList(args).contains("-s");
            if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*"))
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    p.setGameMode(GameMode.CREATIVE);
                    if (!silent)
                        p.sendMessage(CC.t("&6Gamemode: &acreative"));
                }
            else {
                final Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return;
                }
                target.setGameMode(GameMode.CREATIVE);
                if (!silent)
                    target.sendMessage(CC.t("&6Gamemode: &acreative"));
                sender.sendMessage(CC.tf("&6Gamemode for &7%s&6: &acreative", target.getName()));
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> names = new java.util.ArrayList<>();
            String prefix = args[0].toLowerCase();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String n = p.getName();
                    if (n != null && n.toLowerCase().startsWith(prefix))
                        names.add(n);
                }
            }
            return names;
        } else return Collections.emptyList();
    }

}
