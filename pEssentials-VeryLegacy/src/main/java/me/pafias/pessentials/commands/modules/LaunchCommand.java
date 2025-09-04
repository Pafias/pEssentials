package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.events.PlayerLaunchedEvent;
import me.pafias.pessentials.util.CC;
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
            sender.sendMessage(CC.tf("&c/%s <player>", label));
            return;
        }
        final Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        double height = 1.5;
        if (args.length >= 2)
            try {
                height = Double.parseDouble(args[1]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(CC.t("&cInvalid number."));
                return;
            }
        target.setVelocity(target.getVelocity().add(new Vector(0, height, 0)));
        plugin.getServer().getPluginManager().callEvent(new PlayerLaunchedEvent(sender, target, height));
        sender.sendMessage(CC.tf("&aLaunched &d%s", target.getName()));
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
