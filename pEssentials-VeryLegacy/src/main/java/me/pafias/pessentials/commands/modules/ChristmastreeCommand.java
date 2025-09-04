package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Tasks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChristmastreeCommand extends ICommand {

    public ChristmastreeCommand() {
        super("christmastree", "essentials.christmastree", "Spawn a beautiful christmas tree", "/christmastree <width> <height> <base>", "xmastree");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(CC.tf("&c/%s <width> <height> <base>", label));
            return;
        }
        final Player player = (Player) sender;

        Tasks.runAsync(new BukkitRunnable() {
            @Override
            public void run() {
                final Location center = player.getLocation();
                final int width = Integer.parseInt(args[0]);
                final int height = Integer.parseInt(args[1]);
                final int base = Integer.parseInt(args[2]);

                // Base
                for (int y = 0; y < base; y++) {
                    for (double x = -(width / 5.0); x <= (width / 5.0); x++) {
                        for (double z = -(width / 5.0); z <= (width / 5.0); z++) {
                            final double finalX = x;
                            final int finalY = y;
                            final double finalZ = z;

                            Tasks.runSync(new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Block block = center.clone().add(finalX, finalY, finalZ).getBlock();
                                    block.setType(Material.LOG);
                                }
                            });
                        }
                    }
                }

                // Leaves + Snow + Ornaments
                final Random random = new Random();
                final Material[] ornaments = new Material[]{Material.REDSTONE_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.GLOWSTONE};

                final double gradient = width / (double) height;
                double radius = width;

                for (int y = base; y < height + base; y++) {
                    for (double x = -radius; x <= radius; x++) {
                        for (double z = -radius; z <= radius; z++) {
                            final Block block = center.clone().add(x, y, z).getBlock();

                            if (center.clone().add(0, y, 0).distance(block.getLocation()) <= radius) {
                                Tasks.runSync(new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        block.setTypeIdAndData(Material.LEAVES.getId(), (byte) 1, false);
                                        if (random.nextInt(100) < 33) {
                                            block.getRelative(BlockFace.UP).setType(ornaments[random.nextInt(ornaments.length)]);
                                        } else {
                                            block.getRelative(BlockFace.UP).setType(Material.SNOW);
                                        }
                                    }
                                });
                            }
                        }
                    }
                    radius -= gradient;
                }

                // Top
                Tasks.runSync(new BukkitRunnable() {
                    @Override
                    public void run() {
                        Block block = center.clone().add(0, height + base, 0).getBlock();
                        block.setType(Material.BEACON);
                    }
                });
            }
        });
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
