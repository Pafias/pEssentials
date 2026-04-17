package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.putils.LCC;
import me.pafias.putils.Tasks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RussianrouletteCommand extends ICommand {

    public RussianrouletteCommand() {
        super("russianroulette", "essentials.russianroulette", "Russian roulette!", "/russianroulette [chance]");
        cooldownTicks = getPlugin().getConfig().getInt("russian_roulette_cooldown_seconds", 30) * 20L;
    }

    private final Random random = new Random();
    private final long cooldownTicks;
    private final Set<UUID> cooldown = new HashSet<>();

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LCC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        if (cooldown.contains(player.getUniqueId())) {
            sender.sendMessage(LCC.t("&cYou are on cooldown."));
            return;
        }
        final int num1 = random.nextInt(6);
        final int num2 = random.nextInt(6);
        if (num1 == num2) {
            try {
                player.sendTitle(LCC.t("&c&lUh oh!"), LCC.t("&4&lYou were unlucky!"), 2, 20, 20);
            } catch (Throwable t) {
                player.sendTitle(LCC.t("&c&lUh oh!"), LCC.t("&4&lYou were unlucky!"));
            }
            Tasks.runLaterSync(20, () -> {
                try {
                    plugin.getSM().getUserManager().getUser(player).crash();
                } catch (Exception ex) {
                    player.kickPlayer(LCC.t("&k|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"));
                }
            });
        } else {
            sender.sendMessage(LCC.t("&aPhew! You survived."));
            cooldown.add(((Player) sender).getUniqueId());
            Tasks.runLaterAsync(cooldownTicks, () -> cooldown.remove(player.getUniqueId()));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
