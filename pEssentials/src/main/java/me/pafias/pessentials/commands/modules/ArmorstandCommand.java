package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
        if (args.length == 0) {
            help(sender, label);
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        if (args[0].equalsIgnoreCase("spawn")) {
            player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            player.sendMessage(CC.t("&aArmorStand spawned."));
        } else if (args[0].equalsIgnoreCase("arms")) {
            if (!player.isInsideVehicle()) {
                sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                return;
            }
            final Entity vehicle = player.getVehicle();
            if (!(vehicle instanceof ArmorStand as)) {
                sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                return;
            }
            as.setArms(!as.hasArms());
            sender.sendMessage(CC.t("&6ArmorStand arms: " + (as.hasArms() ? "&aON" : "&cOFF")));
        } else if (args[0].equalsIgnoreCase("plate") || args[0].equalsIgnoreCase("baseplate")) {
            if (!player.isInsideVehicle()) {
                sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                return;
            }
            final Entity vehicle = player.getVehicle();
            if (!(vehicle instanceof ArmorStand as)) {
                sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                return;
            }
            as.setBasePlate(!as.hasBasePlate());
            sender.sendMessage(CC.t("&6ArmorStand baseplate: " + (as.hasBasePlate() ? "&aON" : "&cOFF")));
        } else if (args[0].equalsIgnoreCase("size")) {
            if (!player.isInsideVehicle()) {
                sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                return;
            }
            final Entity vehicle = player.getVehicle();
            if (!(vehicle instanceof ArmorStand as)) {
                sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                return;
            }
            as.setSmall(!as.isSmall());
            sender.sendMessage(CC.t("&6ArmorStand size: " + (as.isSmall() ? "&bsmall" : "&bnormal")));
        } else if (args[0].equalsIgnoreCase("marker")) {
            if (!player.isInsideVehicle()) {
                sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                return;
            }
            final Entity vehicle = player.getVehicle();
            if (!(vehicle instanceof ArmorStand as)) {
                sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                return;
            }
            as.setMarker(!as.isMarker());
            sender.sendMessage(CC.t("&6ArmorStand collision box size: " + (as.isMarker() ? "&bsmall" : "&bnormal")));
        } else if (args[0].equalsIgnoreCase("setmainhand")) {
            if (!player.isInsideVehicle()) {
                sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                return;
            }
            final Entity vehicle = player.getVehicle();
            if (!(vehicle instanceof ArmorStand as)) {
                sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                return;
            }

            final ItemStack item = player.getInventory().getItemInHand();
            as.getEquipment().setItemInHand(item);
            sender.sendMessage(CC.t("&aArmorStand main hand item set."));
        } else if (args[0].equalsIgnoreCase("setoffhand")) {
            if (!player.isInsideVehicle()) {
                sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                return;
            }
            final Entity vehicle = player.getVehicle();
            if (!(vehicle instanceof ArmorStand as)) {
                sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                return;
            }

            final ItemStack item = player.getInventory().getItemInMainHand();
            as.getEquipment().setItemInOffHand(item);
            sender.sendMessage(CC.t("&aArmorStand off-hand item set."));
        } else if (args[0].equalsIgnoreCase("setpose")) {
            if (args.length < 5) {
                help(sender, label);
                return;
            }
            if (!player.isInsideVehicle()) {
                sender.sendMessage(CC.t("&cYou need to be on the target armorstand to do this. Use &b/e ride"));
                return;
            }
            final Entity vehicle = player.getVehicle();
            if (!(vehicle instanceof ArmorStand as)) {
                sender.sendMessage(CC.t("&cYou are not on an ArmorStand."));
                return;
            }

            double x, y, z;
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
                    as.setHeadPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
                    break;
                case "body":
                    as.setBodyPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
                    break;
                case "larm":
                    as.setLeftArmPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
                    break;
                case "rarm":
                    as.setRightArmPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
                    break;
                case "lleg":
                    as.setLeftLegPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
                    break;
                case "rleg":
                    as.setRightLegPose(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
                    break;
            }

            sender.sendMessage(CC.t("&aPose changed."));
        } else {
            help(sender, label);
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return Stream.of("spawn", "arms", "plate", "size", "marker", "setmainhand", "setoffhand", "setpose")
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        else if (args.length == 2 && args[0].equalsIgnoreCase("setpose"))
            return Stream.of("head", "body", "Larm", "Rarm", "Lleg", "Rleg")
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        else if ((args.length == 3 || args.length == 4 || args.length == 5) && args[0].equalsIgnoreCase("setpose"))
            return Collections.singletonList("x y z");
        else return Collections.emptyList();
    }

}
