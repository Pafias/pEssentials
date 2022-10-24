package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class RussianrouletteCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public RussianrouletteCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    private Set<UUID> cooldown = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return true;
        }
        if (sender.hasPermission("essentials.russianroulette")) {
            Player player = (Player) sender;
            if (cooldown.contains(player.getUniqueId())) {
                sender.sendMessage(CC.t("&cYou are on cooldown."));
                return true;
            }
            int num1 = new Random().nextInt(6);
            int num2 = new Random().nextInt(6);
            if (num1 == num2) {
                player.sendTitle(CC.t("&c&lUh oh!"), CC.t("&4&lYou were unlucky!"), 2, 20, 20);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getSM().getNMSProvider().crash(player);
                    }
                }.runTaskLater(plugin, 20);
            } else {
                sender.sendMessage(CC.t("&aPhew! You survived."));
                cooldown.add(((Player) sender).getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        cooldown.remove(((Player) sender).getUniqueId());
                    }
                }.runTaskLaterAsynchronously(plugin, (5 * 60 * 20));
            }
        }
        return true;
    }

}
