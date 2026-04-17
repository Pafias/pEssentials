package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GmsCommand extends ICommand {

    public GmsCommand() {
        super("gms", "essentials.gamemode", "Gamemode survival", "/gms [player]", "gm0");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(GamemodeCommand.gamemodePermissions.get(GameMode.SURVIVAL))) {
            sender.sendMessage(LCC.t("&cYou do not have permission for this gamemode!"));
            return;
        }
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LCC.t("&cOnly players!"));
                return;
            }
            final Player player = (Player) sender;
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(LCC.t("&6Gamemode: &asurvival"));
        } else {
            if (!sender.hasPermission(getPermission() + ".others")) {
                sender.sendMessage(LCC.t("&cYou do not have permission to change other players' gamemodes!"));
                return;
            }
            boolean silent = Arrays.asList(args).contains("-s");
            if (args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("*"))
                plugin.getServer().getOnlinePlayers().forEach(p -> {
                    p.setGameMode(GameMode.SURVIVAL);
                    if (!silent)
                        p.sendMessage(LCC.t("&6Gamemode: &asurvival"));
                });
            else {
                final Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
                    sender.sendMessage(LCC.t("&cPlayer not found!"));
                    return;
                }
                target.setGameMode(GameMode.SURVIVAL);
                if (!silent)
                    target.sendMessage(LCC.t("&6Gamemode: &asurvival"));
                sender.sendMessage(LCC.tf("&6Gamemode for &7%s&6: &asurvival", target.getName()));
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return RandomUtils.tabCompletePlayers(sender, args[0]);
    }

}
