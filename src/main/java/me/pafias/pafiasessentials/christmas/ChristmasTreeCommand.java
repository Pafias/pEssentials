package me.pafias.pafiasessentials.christmas;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ChristmasTreeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.christmastree")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cOnly players!"));
                return true;
            }
            Player player = (Player) sender;

            Location center = player.getLocation();
            int width = Integer.parseInt(args[0]);
            int height = Integer.parseInt(args[1]);
            int base = Integer.parseInt(args[2]);

            // Base (doesn't work lol)
            for (int y = center.getBlockY(); y < base; y++) {
                for (double x = -width; x <= Math.round((double) width / 4); x++) {
                    for (double z = -width; z <= Math.round((double) width / 4); z++) {
                        Block block = center.clone().add(x, y, z).getBlock();
                        block.setType(Material.SPRUCE_LOG, false);
                    }
                }
            }

            // Leaves + Snow + Ornaments
            Random random = new Random();
            Material[] ornaments = new Material[]{Material.REDSTONE_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.GLOWSTONE};

            final double gradient = width / (double) height;
            double radius = width;

            for (int y = base; y < height + base; y++) {
                for (double x = -radius; x <= radius; x++) {
                    for (double z = -radius; z <= radius; z++) {
                        Block block = center.clone().add(x, y, z).getBlock();

                        if (center.clone().add(0, y, 0).distance(block.getLocation()) <= radius) {
                            block.setType(Material.OAK_LEAVES, false);
                            if (random.nextInt(100) < 33) {
                                block.getRelative(BlockFace.UP).setType(ornaments[random.nextInt(ornaments.length)]);
                            } else {
                                block.getRelative(BlockFace.UP).setType(Material.SNOW);
                            }
                        }
                    }
                }
                radius -= gradient;
            }

            // Top
            Block block = center.clone().add(0, height + base, 0).getBlock();
            block.setType(Material.BEACON);

        }
        return true;
    }

}
