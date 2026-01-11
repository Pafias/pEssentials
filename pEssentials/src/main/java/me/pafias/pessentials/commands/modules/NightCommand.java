package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NightCommand extends ICommand {

    public NightCommand() {
        super("night", null, "Make it night", "/night", "nighttime");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.night") && Arrays.stream(args).noneMatch(arg -> arg.toLowerCase().contains("-p"))) {
            World world;
            if (args.length >= 1)
                world = plugin.getServer().getWorld(args[0]);
            else {
                world = plugin.getServer().getWorlds().get(0);
            }
            if (world == null) {
                sender.sendMessage(CC.t("&cInvalid world!"));
                return;
            }
            world.setTime(14000);
            sender.sendMessage(CC.t("&6Time set to night (14000 ticks)"));
        } else if (sender.hasPermission("essentials.night.personal")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            if (!player.isPlayerTimeRelative()) {
                player.resetPlayerTime();
                player.sendMessage(CC.t("&6Personal time reset."));
            } else {
                player.setPlayerTime(14000, false);
                player.sendMessage(CC.t("&6Personal time set to night (14000 ticks)"));
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
