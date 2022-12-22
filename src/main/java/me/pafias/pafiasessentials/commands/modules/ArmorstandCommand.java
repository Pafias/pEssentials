package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArmorstandCommand extends ICommand {

    public ArmorstandCommand() {
        super("armorstand", "essentials.armorstand", "ArmorStand manipulation", "/armorstand [subcommand]", "as");
    }

    private void help(CommandSender sender, String label) {
        sender.sendMessage(CC.t("&2/" + label + " spawn &f- Spawn an armorstand"));
        sender.sendMessage(CC.t("&2/" + label + " arms &f- Toggle armorstand arms"));
        sender.sendMessage(CC.t("&2/" + label + " plate &f- Toggle armorstand baseplate"));
        sender.sendMessage(CC.t("&2/" + label + " size &f- Toggle armorstand size"));
        sender.sendMessage(CC.t("&2/" + label + " marker &f- Toggle armorstand collision box size"));
        sender.sendMessage(CC.t("&2/" + label + " setmainhand &f- Set the armorstand main hand item"));
        sender.sendMessage(CC.t("&2/" + label + " setoffhand &f- Set the armorstand off-hand item"));
        sender.sendMessage(CC.t("&2/" + label + " setpose <head/body/Larm/Rarm/Lleg/Rleg> <x> <y> <z> &f- Set the armorstand pose"));
        sender.sendMessage(CC.t("&7&oFor more commands, check the /e command"));
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.armorstand")) {
            if (args.length == 0) {
                help(sender, label);
                return;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("spawn")) {
                player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                player.sendMessage(CC.t("&aArmorStand spawned."));
                return;
            } else if (args[0].equalsIgnoreCase("arms")) {
                if (!player.isInsideVehicle()) {
                    sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                    return;
                }
                Entity vehicle = player.getVehicle();
                if (!(vehicle instanceof ArmorStand)) {
                    sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                    return;
                }
                ArmorStand as = (ArmorStand) vehicle;
                as.setArms(!as.hasArms());
                sender.sendMessage(CC.t("&6ArmorStand arms: " + (as.hasArms() ? "&aON" : "&cOFF")));
                return;
            } else if (args[0].equalsIgnoreCase("plate") || args[0].equalsIgnoreCase("baseplate")) {
                if (!player.isInsideVehicle()) {
                    sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                    return;
                }
                Entity vehicle = player.getVehicle();
                if (!(vehicle instanceof ArmorStand)) {
                    sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                    return;
                }
                ArmorStand as = (ArmorStand) vehicle;
                as.setBasePlate(!as.hasBasePlate());
                sender.sendMessage(CC.t("&6ArmorStand baseplate: " + (as.hasBasePlate() ? "&aON" : "&cOFF")));
                return;
            } else if (args[0].equalsIgnoreCase("size")) {
                if (!player.isInsideVehicle()) {
                    sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                    return;
                }
                Entity vehicle = player.getVehicle();
                if (!(vehicle instanceof ArmorStand)) {
                    sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                    return;
                }
                ArmorStand as = (ArmorStand) vehicle;
                as.setSmall(!as.isSmall());
                sender.sendMessage(CC.t("&6ArmorStand size: " + (as.isSmall() ? "&bsmall" : "&bnormal")));
                return;
            } else if (args[0].equalsIgnoreCase("marker")) {
                if (!player.isInsideVehicle()) {
                    sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                    return;
                }
                Entity vehicle = player.getVehicle();
                if (!(vehicle instanceof ArmorStand)) {
                    sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                    return;
                }
                ArmorStand as = (ArmorStand) vehicle;
                as.setMarker(!as.isMarker());
                sender.sendMessage(CC.t("&6ArmorStand collision box size: " + (as.isMarker() ? "&bsmall" : "&bnormal")));
                return;
            } else if (args[0].equalsIgnoreCase("setmainhand")) {
                if (!player.isInsideVehicle()) {
                    sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                    return;
                }
                Entity vehicle = player.getVehicle();
                if (!(vehicle instanceof ArmorStand)) {
                    sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                    return;
                }
                ArmorStand as = (ArmorStand) vehicle;

                ItemStack item = player.getInventory().getItemInMainHand();
                as.getEquipment().setItemInMainHand(item);
                sender.sendMessage(CC.t("&aArmorStand main hand item set."));
                return;
            } else if (args[0].equalsIgnoreCase("setoffhand")) {
                if (!player.isInsideVehicle()) {
                    sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                    return;
                }
                Entity vehicle = player.getVehicle();
                if (!(vehicle instanceof ArmorStand)) {
                    sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                    return;
                }
                ArmorStand as = (ArmorStand) vehicle;

                ItemStack item = player.getInventory().getItemInMainHand();
                as.getEquipment().setItemInOffHand(item);
                sender.sendMessage(CC.t("&aArmorStand off-hand item set."));
                return;
            } else if (args[0].equalsIgnoreCase("setpose")) {
                if (args.length < 5) {
                    help(sender, label);
                    return;
                }
                if (!player.isInsideVehicle()) {
                    sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                    return;
                }
                Entity vehicle = player.getVehicle();
                if (!(vehicle instanceof ArmorStand)) {
                    sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                    return;
                }
                ArmorStand as = (ArmorStand) vehicle;

                double x;
                double y;
                double z;
                try {
                    x = Double.parseDouble(args[2]);
                    y = Double.parseDouble(args[3]);
                    z = Double.parseDouble(args[4]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid angle values."));
                    return;
                }

                switch (args[1].toLowerCase()) {
                    case "head":
                        as.setHeadPose(new EulerAngle(Math.toDegrees(x), Math.toDegrees(y), Math.toDegrees(z)));
                        break;
                    case "body":
                        as.setBodyPose(new EulerAngle(Math.toDegrees(x), Math.toDegrees(y), Math.toDegrees(z)));
                        break;
                    case "larm":
                        as.setLeftArmPose(new EulerAngle(Math.toDegrees(x), Math.toDegrees(y), Math.toDegrees(z)));
                        break;
                    case "rarm":
                        as.setRightArmPose(new EulerAngle(Math.toDegrees(x), Math.toDegrees(y), Math.toDegrees(z)));
                        break;
                    case "lleg":
                        as.setLeftLegPose(new EulerAngle(Math.toDegrees(x), Math.toDegrees(y), Math.toDegrees(z)));
                        break;
                    case "rleg":
                        as.setRightLegPose(new EulerAngle(Math.toDegrees(x), Math.toDegrees(y), Math.toDegrees(z)));
                        break;
                }

                sender.sendMessage(CC.t("&aPose changed."));
                return;

            } else {
                help(sender, label);
                return;
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return Arrays.asList("spawn", "arms", "plate", "size", "marker", "setmainhand", "setoffhand", "setpose");
        else if (args.length == 2 && args[0].equalsIgnoreCase("setpose"))
            return Arrays.asList("head", "body", "Larm", "Rarm", "Lleg", "Rleg");
        else if ((args.length == 3 || args.length == 4 || args.length == 5) && args[0].equalsIgnoreCase("setpose"))
            return Collections.singletonList("x y z");
        else return Collections.emptyList();
    }

}
