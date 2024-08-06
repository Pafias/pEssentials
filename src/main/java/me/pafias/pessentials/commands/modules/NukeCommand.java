package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collections;
import java.util.List;

public class NukeCommand extends ICommand {

    public NukeCommand() {
        super("nuke", "essentials.nuke", "NUKE", "/nuke");
    }

    @EventHandler
    public void onExplode(ExplosionPrimeEvent event) {
        if (event.getEntity().hasMetadata("fake"))
            event.setCancelled(true);
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players"));
            return;
        }
        Player player = (Player) sender;
        for (int x = 0; x < 15; x++) {
            for (int z = 0; z < 15; z++) {
                player.getLocation().getWorld().spawn(player.getLocation().add(x % 2 == 0 ? x : 0, 0, z % 2 == 0 ? z : 0), TNTPrimed.class, tnt -> {
                    tnt.setMetadata("fake", new FixedMetadataValue(plugin, true));
                    tnt.setIsIncendiary(false);
                    tnt.setYield(5);
                });
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
