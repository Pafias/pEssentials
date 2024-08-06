package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LightningCommand extends ICommand {

    public LightningCommand() {
        super("lightning", "essentials.lightning", "Strike a lightning bolt!", "/lightning [player]", "strike");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players"));
                return;
            }
            Player player = (Player) sender;
            Location location;
            Entity targetEntity = player.getTargetEntity(32);
            if (targetEntity != null) {
                location = targetEntity.getLocation();
            } else {
                Block target = player.getTargetBlock(32);
                if (target != null) {
                    location = target.getLocation();
                } else {
                    sender.sendMessage(CC.t("&cPlease look something to strike"));
                    return;
                }
            }
            if (location == null) {
                sender.sendMessage(CC.t("&cInvalid location to strike"));
                return;
            }
            location.getWorld().strikeLightning(location);
        } else {
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cInvalid player"));
                return;
            }
            Location location = target.getLocation();
            location.getWorld().strikeLightning(location);
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }

}
