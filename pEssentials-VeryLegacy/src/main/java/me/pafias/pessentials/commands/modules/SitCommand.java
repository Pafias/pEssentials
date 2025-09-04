package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class SitCommand extends ICommand {

    public static Map<HumanEntity, ExperienceOrb> map = new WeakHashMap<>();

    public SitCommand() {
        super("sit", "essentials.sit", "Sit down", "/sit", "chair");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        if (map.containsKey(player)) {
            map.get(player).remove();
            map.remove(player);
        } else {
            final Location location = player.getLocation().clone();
            try {
                location.subtract(0, player.getEyeHeight(), 0);
            } catch (Throwable t) {
                location.subtract(0, 1, 0);
            }
            location.add(0, 0.1, 0);
            final ExperienceOrb exp = (ExperienceOrb) player.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
            exp.setPassenger(player);
            map.put(player, exp);
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
