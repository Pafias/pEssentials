package me.pafias.pafiasessentials.christmas;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Random;

public class ChristmasTreeCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public ChristmasTreeCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.christmastree")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(CC.tf("&c/%s <width> <height> <base>", label));
                return true;
            }
            Player player = (Player) sender;

            new BukkitRunnable() {
                @Override
                public void run() {
                    Location center = player.getLocation();
                    int width = Integer.parseInt(args[0]);
                    int height = Integer.parseInt(args[1]);
                    int base = Integer.parseInt(args[2]);

                    // Base
                    for (int y = 0; y < base; y++) {
                        for (double x = -(width / 5.0); x <= (width / 5.0); x++) {
                            for (double z = -(width / 5.0); z <= (width / 5.0); z++) {
                                double finalX = x;
                                int finalY = y;
                                double finalZ = z;
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Block block = center.clone().add(finalX, finalY, finalZ).getBlock();
                                        if (plugin.parseVersion() < 13)
                                            block.setType(Material.getMaterial("LOG"));
                                        else
                                            block.setType(Material.getMaterial("SPRUCE_LOG"), false);
                                    }
                                }.runTask(plugin);
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
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (plugin.parseVersion() < 13) {
                                                try {
                                                    Class blockClass = Class.forName("org.bukkit.block.Block");
                                                    Method setTypeIdAndData = blockClass.getMethod("setTypeIdAndData", int.class, byte.class, boolean.class);
                                                    setTypeIdAndData.invoke(block, Material.getMaterial("LEAVES").getId(), (byte) 1, false);
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            } else {
                                                block.setType(Material.OAK_LEAVES, false);
                                                BlockData data = block.getBlockData();
                                                ((Leaves) data).setPersistent(true);
                                                block.setBlockData(data, false);
                                            }
                                            if (random.nextInt(100) < 33) {
                                                block.getRelative(BlockFace.UP).setType(ornaments[random.nextInt(ornaments.length)]);
                                            } else {
                                                block.getRelative(BlockFace.UP).setType(Material.SNOW);
                                            }
                                        }
                                    }.runTask(plugin);
                                }
                            }
                        }
                        radius -= gradient;
                    }

                    // Top
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Block block = center.clone().add(0, height + base, 0).getBlock();
                            block.setType(Material.BEACON);
                        }
                    }.runTask(plugin);

                }
            }.runTaskAsynchronously(plugin);

        }
        return true;
    }

}
