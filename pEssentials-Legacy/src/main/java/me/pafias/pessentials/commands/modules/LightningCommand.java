package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class LightningCommand extends ICommand {

    public LightningCommand() {
        super("lightning", "essentials.lightning", "Strike a lightning bolt!", "/lightning [player]", "strike");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LCC.t("&cOnly players"));
                return;
            }
            final Player player = (Player) sender;
            Location location;
            final Entity targetEntity = player.getTargetEntity(32);
            if (targetEntity != null) {
                location = targetEntity.getLocation();
            } else {
                Block target = player.getTargetBlock(32);
                if (target != null) {
                    location = target.getLocation();
                } else {
                    sender.sendMessage(LCC.t("&cPlease look something to strike"));
                    return;
                }
            }
            if (location == null) {
                sender.sendMessage(LCC.t("&cInvalid location to strike"));
                return;
            }
            location.getWorld().strikeLightning(location);
        } else {
            final Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
                sender.sendMessage(LCC.t("&cInvalid player"));
                return;
            }
            final Location location = target.getLocation();
            location.getWorld().strikeLightning(location);
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        return Collections.emptyList();
    }

}
