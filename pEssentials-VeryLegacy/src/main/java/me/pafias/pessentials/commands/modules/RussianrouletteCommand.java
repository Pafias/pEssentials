package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Tasks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RussianrouletteCommand extends ICommand {

    public RussianrouletteCommand() {
        super("russianroulette", "essentials.russianroulette", "Russian roulette!", "/russianroulette [chance]");
    }

    private final Set<UUID> cooldown = new HashSet<>();

    @Override
    public void commandHandler(@NotNull final CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        if (cooldown.contains(player.getUniqueId())) {
            sender.sendMessage(CC.t("&cYou are on cooldown."));
            return;
        }
        final int num1 = new Random().nextInt(6);
        final int num2 = new Random().nextInt(6);
        if (num1 == num2) {
            player.sendMessage(CC.t("&c&lUh oh! &4&lYou were unlucky!"));
            Tasks.runLaterSync(20, new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        plugin.getSM().getUserManager().getUser(player).crash();
                    } catch (Throwable ex) {
                        player.kickPlayer(CC.t("&k|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"));
                    }
                }
            });
        } else {
            sender.sendMessage(CC.t("&aPhew! You survived."));
            cooldown.add(((Player) sender).getUniqueId());
            Tasks.runLaterAsync(600, new BukkitRunnable() {
                @Override
                public void run() {
                    cooldown.remove(((Player) sender).getUniqueId());
                }
            });
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
