package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.events.PlayerLaunchedEvent;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class LaunchCommand extends ICommand {

    public LaunchCommand() {
        super("launch", "essentials.launch", "Launch someone into the air", "/launch <player> [height]");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(LCC.tf("&c/%s <player>", label));
            return;
        }
        final Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
            sender.sendMessage(LCC.t("&cPlayer not found!"));
            return;
        }
        double height = 1.5;
        if (args.length >= 2)
            try {
                height = Double.parseDouble(args[1]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(LCC.t("&cInvalid number."));
                return;
            }
        target.setVelocity(target.getVelocity().add(new Vector(0, height, 0)));
        plugin.getServer().getPluginManager().callEvent(new PlayerLaunchedEvent(sender, target, height));
        sender.sendMessage(LCC.tf("&aLaunched &d%s", target.getName()));
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        else return Collections.emptyList();
    }

}
