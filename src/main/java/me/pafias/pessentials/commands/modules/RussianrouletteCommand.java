package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RussianrouletteCommand extends ICommand {

    public RussianrouletteCommand() {
        super("russianroulette", "essentials.russianroulette", "Russian roulette!", "/russianroulette");
    }

    private Set<UUID> cooldown = new HashSet<>();

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        if (sender.hasPermission("essentials.russianroulette")) {
            Player player = (Player) sender;
            if (cooldown.contains(player.getUniqueId())) {
                sender.sendMessage(CC.t("&cYou are on cooldown."));
                return;
            }
            int num1 = new Random().nextInt(6);
            int num2 = new Random().nextInt(6);
            if (num1 == num2) {
                player.sendTitle(CC.t("&c&lUh oh!"), CC.t("&4&lYou were unlucky!"), 2, 20, 20);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getSM().getUserManager().getUser(player).crash();
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
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
