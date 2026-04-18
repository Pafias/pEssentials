package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.putils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.RayTraceResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ArmorstandCommand extends ICommand {

    public ArmorstandCommand() {
        super("armorstand", "essentials.armorstand", "ArmorStand manipulation", "/armorstand [subcommand]", "as");
    }

    private static final int ARMORSTAND_RESOLVE_RANGE = 5;

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players."));
            return;
        }
        if (args.length == 0) {
            help(sender, label);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "spawn", "s" -> handleSpawn(player);
            case "arms" -> handleArms(player);
            case "plate", "baseplate" -> handlePlate(player);
            case "size" -> handleSize(player);
            case "marker" -> handleMarker(player);
            case "mainhand", "setmainhand" -> handleMainHand(player);
            case "offhand", "setoffhand" -> handleOffHand(player);
            case "pose", "setpose" -> handlePose(player, label, args);
            default -> help(sender, label);
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            final String arg = args[0].toLowerCase();
            return Stream.of("spawn", "arms", "plate", "size", "marker", "mainhand", "offhand", "pose")
                    .filter(s -> s.toLowerCase().contains(arg))
                    .toList();
        } else if (args.length > 1 && isAny(args[0], "setpose", "pose")) {
            if (args.length == 2) {
                final String arg = args[1].toLowerCase();
                return Stream.of("head", "body", "Larm", "Rarm", "Lleg", "Rleg")
                        .filter(s -> s.toLowerCase().startsWith(arg))
                        .toList();
            }
            if (args.length > 5)
                return Collections.emptyList();
            String[] angles = new String[]{"0", "0", "0"};
            for (int i = 2; i <= 4; i++) {
                if (args.length > i) {
                    String arg = args[i].isBlank() ? "0" : args[i];
                    try {
                        Double.parseDouble(arg);
                        angles[i - 2] = arg;
                    } catch (NumberFormatException ex) {
                        return Collections.singletonList(CC.t("&cInvalid angle value: " + arg));
                    }
                }
            }
            return Collections.singletonList(String.join(" ", angles));
        } else return Collections.emptyList();
    }

    private boolean isAny(String value, String... options) {
        for (String option : options) {
            if (option.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private void help(CommandSender sender, String label) {
        sender.sendMessage(CC.t("&2/" + label + " spawn &7- Spawn an armorstand"));
        sender.sendMessage(CC.t("&2/" + label + " arms &7- Toggle armorstand arms"));
        sender.sendMessage(CC.t("&2/" + label + " plate &7- Toggle armorstand baseplate"));
        sender.sendMessage(CC.t("&2/" + label + " size &7- Toggle armorstand size"));
        sender.sendMessage(CC.t("&2/" + label + " marker &7- Toggle armorstand collision box size"));
        sender.sendMessage(CC.t("&2/" + label + " mainhand &7- Set the armorstand main hand item"));
        sender.sendMessage(CC.t("&2/" + label + " offhand &7- Set the armorstand off-hand item"));
        sender.sendMessage(CC.t("&2/" + label + " pose <body part> <xyz> &7- Set the armorstand pose"));
        sender.sendMessage(CC.t("&7&oFor more (general) commands, check the /e command"));
    }

    private ArmorStand resolveArmorstand(Player player) {
        if (player.isInsideVehicle() && player.getVehicle() instanceof ArmorStand as)
            return as;

        final RayTraceResult raytrace = player.rayTraceEntities(ARMORSTAND_RESOLVE_RANGE, true);
        if (raytrace != null) {
            final Entity traced = raytrace.getHitEntity();
            if (traced != null && traced instanceof ArmorStand as)
                return as;
        }

        for (Entity nearby : player.getNearbyEntities(ARMORSTAND_RESOLVE_RANGE, ARMORSTAND_RESOLVE_RANGE, ARMORSTAND_RESOLVE_RANGE)) {
            if (!(nearby instanceof ArmorStand as))
                continue;
            return as;
        }

        return null;
    }

    private void handleSpawn(Player player) {
        player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        player.sendActionBar(CC.t("&aArmorStand spawned."));
    }

    private void handleArms(Player player) {
        final ArmorStand as = resolveArmorstand(player);
        if (as == null) {
            player.sendMessage(CC.t("&cNo armorstand found."));
            return;
        }
        as.setArms(!as.hasArms());
        player.sendActionBar(CC.t("&6ArmorStand arms: " + (as.hasArms() ? "&aON" : "&cOFF")));
    }

    private void handlePlate(Player player) {
        final ArmorStand as = resolveArmorstand(player);
        if (as == null) {
            player.sendMessage(CC.t("&cNo armorstand found."));
            return;
        }
        as.setBasePlate(!as.hasBasePlate());
        player.sendActionBar(CC.t("&6ArmorStand baseplate: " + (as.hasBasePlate() ? "&aON" : "&cOFF")));
    }

    private void handleSize(Player player) {
        final ArmorStand as = resolveArmorstand(player);
        if (as == null) {
            player.sendMessage(CC.t("&cNo armorstand found."));
            return;
        }
        as.setSmall(!as.isSmall());
        player.sendActionBar(CC.t("&6ArmorStand size: " + (as.isSmall() ? "&bsmall" : "&bnormal")));
    }

    private void handleMarker(Player player) {
        final ArmorStand as = resolveArmorstand(player);
        if (as == null) {
            player.sendMessage(CC.t("&cNo armorstand found."));
            return;
        }
        as.setMarker(!as.isMarker());
        player.sendActionBar(CC.t("&6ArmorStand collision box size: " + (as.isMarker() ? "&bsmall" : "&bnormal")));
    }

    private void handleMainHand(Player player) {
        final ArmorStand as = resolveArmorstand(player);
        if (as == null) {
            player.sendMessage(CC.t("&cNo armorstand found."));
            return;
        }

        final ItemStack item = player.getInventory().getItemInMainHand();
        as.getEquipment().setItemInMainHand(item);
        player.sendActionBar(CC.t("&aArmorStand main hand item set."));
    }

    private void handleOffHand(Player player) {
        final ArmorStand as = resolveArmorstand(player);
        if (as == null) {
            player.sendMessage(CC.t("&cNo armorstand found."));
            return;
        }

        final ItemStack item = player.getInventory().getItemInMainHand();
        as.getEquipment().setItemInOffHand(item);
        player.sendActionBar(CC.t("&aArmorStand off-hand item set."));
    }

    private void handlePose(Player player, String label, String[] args) {
        if (args.length < 5) {
            player.sendMessage(CC.t("&c/" + label + " " + args[0] + " <head/body/Larm/Rarm/Lleg/Rleg> <x> <y> <z>"));
            return;
        }

        double x, y, z;
        try {
            x = Double.parseDouble(args[2]);
            y = Double.parseDouble(args[3]);
            z = Double.parseDouble(args[4]);
        } catch (NumberFormatException ex) {
            player.sendMessage(CC.t("&cInvalid angle values."));
            return;
        }

        final ArmorStand as = resolveArmorstand(player);
        if (as == null) {
            player.sendMessage(CC.t("&cNo armorstand found."));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "head" -> as.setHeadPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
            case "body" -> as.setBodyPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
            case "larm" -> as.setLeftArmPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
            case "rarm" -> as.setRightArmPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
            case "lleg" -> as.setLeftLegPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
            case "rleg" -> as.setRightLegPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
            default -> {
                player.sendMessage(CC.t("&cInvalid pose part. Use head/body/Larm/Rarm/Lleg/Rleg."));
                return;
            }
        }

        player.sendActionBar(CC.t("&aPose changed."));
    }

}
