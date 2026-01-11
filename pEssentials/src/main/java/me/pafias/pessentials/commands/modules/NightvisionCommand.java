package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class NightvisionCommand extends ICommand {

    public NightvisionCommand() {
        super("nightvision", "essentials.nightvision", "Nightvision", "/nv", "nv");
    }

    private void toggleNightvision(Player player) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1, false, false));
        }
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            toggleNightvision((Player) sender);
        } else if (sender.hasPermission(getPermission() + ".others")) {
            final Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null || (sender instanceof Player senderPlayer && !senderPlayer.canSee(target))) {
                sender.sendMessage(CC.t("&cPlayer not found."));
                return;
            }
            toggleNightvision(target);
            sender.sendMessage(CC.tf("&6Nightvision for %s: %s", target.getName(), target.hasPotionEffect(PotionEffectType.NIGHT_VISION) ? CC.t("&aON") : CC.t("&cOFF")));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
