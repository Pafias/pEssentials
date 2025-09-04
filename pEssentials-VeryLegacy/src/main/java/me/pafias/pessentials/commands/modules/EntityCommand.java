package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Reflection;
import me.pafias.pessentials.util.Tasks;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityCommand extends ICommand {

    public EntityCommand() {
        super("entity", "essentials.entity", "Entity utils", "/e", "e");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        final Player player = (Player) sender;
        final User user = plugin.getSM().getUserManager().getUser(player);
        if (args.length == 0) {
            sender.sendMessage(CC.t("&2/" + label + " s [type] &f- Spawns an entity with you on it"));
            sender.sendMessage(CC.t("&2/" + label + " d &f- Deletes the entity you're on or else the closest one to you"));
            sender.sendMessage(CC.t("&2/" + label + " fly [player] &f- Toggle fly mode for the entity where you/player is sitting on"));
            sender.sendMessage(CC.t("&2/" + label + " age [age] &f- Change the age of the entity where you're sitting on"));
            sender.sendMessage(CC.t("&2/" + label + " tp <player> &f- Teleport together with your entity to someone"));
            sender.sendMessage(CC.t("&2/" + label + " name [name] &f- Change the name of the entity you're sitting on"));
            sender.sendMessage(CC.t("&2/" + label + " ride [-d] &f- Ride on (or dismount -d) the closest entity to you"));
            return;
        }
        if (args[0].equalsIgnoreCase("fly") && sender.hasPermission("essentials.entity.fly")) {
            if (!plugin.getServer().getPluginManager().isPluginEnabled("packetevents")) {
                sender.sendMessage(CC.t("&cPacketEvents not found! This feature only works with PacketEvents!"));
                return;
            }
            final Entity entity = player.getVehicle();
            if (entity == null) {
                sender.sendMessage(CC.t("&cYou're not on an entity!"));
                return;
            }
            user.flyingEntity = !user.flyingEntity;
            Reflection.sendActionbar(user.getPlayer(), CC.t("&6Entity fly: " + (user.flyingEntity ? "&aON" : "&cOFF")));
        } else if (args[0].equalsIgnoreCase("move") && sender.hasPermission("essentials.entity.move")) {
            if (!plugin.getServer().getPluginManager().isPluginEnabled("packetevents")) {
                sender.sendMessage(CC.t("&cPacketEvents not found! This feature only works with PacketEvents!"));
                return;
            }
            final Entity entity = player.getVehicle();
            if (entity == null) {
                sender.sendMessage(CC.t("&cYou're not on an entity!"));
                return;
            }
            user.movingEntity = !user.movingEntity;
            Reflection.sendActionbar(user.getPlayer(), CC.t("&6Entity move: " + (user.movingEntity ? "&aON" : "&cOFF")));
        } else if ((args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("sv")) && sender.hasPermission("essentials.entity.spawn")) {
            Entity entity;
            if (args.length > 1) {
                entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.fromName(args[1]));
            } else {
                entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
            }
            if (entity == null) {
                sender.sendMessage(CC.t("&cInvalid entity!"));
                return;
            }
            if (entity instanceof Tameable) {
                ((Tameable) entity).setTamed(true);
                ((Tameable) entity).setOwner(player);
            }
            if (entity instanceof Ageable)
                ((Ageable) entity).setAdult();
            if (entity instanceof InventoryHolder)
                ((InventoryHolder) entity).getInventory().setItem(0, new ItemStack(Material.SADDLE, 1));
            entity.setPassenger(player);
        } else if ((args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("dv") || args[0].equalsIgnoreCase("remove")) && sender.hasPermission("essentials.entity.delete")) {
            Entity entity = null;
            if (player.isInsideVehicle()) {
                entity = player.getVehicle();
            } else {
                try {
                    for (Entity e : player.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
                        if (!(e instanceof Player)) {
                            entity = e;
                            break;
                        }
                    }
                } catch (IndexOutOfBoundsException ex) {
                    sender.sendMessage(CC.t("&cNo entities found nearby!"));
                    return;
                }
            }
            if (entity == null) {
                sender.sendMessage(CC.t("&cNo entities found nearby!"));
                return;
            }
            entity.remove();
            user.flyingEntity = false;
            user.movingEntity = false;
            Reflection.sendActionbar(user.getPlayer(), CC.t("&6Removed: &r" + entity));
        } else if (args[0].equalsIgnoreCase("age") && sender.hasPermission("essentials.entity.age")) {
            final Entity entity = player.getVehicle();
            if (entity == null) {
                sender.sendMessage(CC.t("&cYou're not on an entity!"));
                return;
            }
            if (!(entity instanceof Ageable)) {
                sender.sendMessage(CC.t("&cThat's not possible with this entity."));
                return;
            }
            if (args.length >= 2) {
                try {
                    ((Ageable) entity).setAge(Integer.parseInt(args[1]));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid age."));
                    return;
                }
            } else if (((Ageable) entity).isAdult()) {
                ((Ageable) entity).setBaby();
            } else {
                ((Ageable) entity).setAdult();
            }
        } else if ((args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) && sender.hasPermission("essentials.entity.teleport")) {
            if (args.length < 2) {
                sender.sendMessage(CC.t("&c/" + label + " " + args[0] + " <player>"));
                return;
            }
            final Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            Entity entity = player.getVehicle();
            if (entity == null)
                entity = player;
            player.teleport(target);
            if (entity != player) {
                entity.teleport(target);
                final Entity finalEntity = entity;
                Tasks.runLaterSync(40, new BukkitRunnable() {
                    @Override
                    public void run() {
                        finalEntity.setPassenger(player);
                    }
                });
            }
        } else if (args[0].equalsIgnoreCase("name") && sender.hasPermission("essentials.entity.name")) {
            final Entity entity = player.getVehicle();
            if (entity == null) {
                sender.sendMessage(CC.t("&cYou're not on an entity!"));
                return;
            }
            if (!(entity instanceof LivingEntity)) {
                sender.sendMessage(CC.t("&cThis entity cannot have a name."));
                return;
            }
            LivingEntity livingEntity = (LivingEntity) entity;
            if (args.length < 2) {
                livingEntity.setCustomNameVisible(!livingEntity.isCustomNameVisible());
                Reflection.sendActionbar(user.getPlayer(), CC.t("&6Entity name visibility: " + (livingEntity.isCustomNameVisible() ? "&aVISIBLE" : "&cINVISIBLE")));
                return;
            }
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(i == args.length - 1 ? "" : " ");
            livingEntity.setCustomNameVisible(true);
            livingEntity.setCustomName(CC.t(sb.toString()));
            Reflection.sendActionbar(user.getPlayer(), CC.t("&6Entity name: &r" + livingEntity.getCustomName()));
        } else if (args[0].equalsIgnoreCase("ride") && sender.hasPermission("essentials.entity.ride")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("-d")) {
                if (!player.isInsideVehicle()) {
                    sender.sendMessage(CC.t("&cYou are not riding anything."));
                    return;
                }
                player.leaveVehicle();
                return;
            }
            double dist = 2.5;
            if (args.length == 2)
                try {
                    dist = Double.parseDouble(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CC.t("&cInvalid distance."));
                    return;
                }
            Entity entity;
            try {
                List<Entity> list = player.getNearbyEntities(dist, dist, dist);
                entity = list.get(0);
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
                sender.sendMessage(CC.t("&cNo entities found nearby."));
                return;
            }
            entity.setPassenger(player);
            Reflection.sendActionbar(user.getPlayer(), CC.t("&6Now riding: &b" + entity));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            String[] cmds = {"spawn", "delete", "fly", "age", "tp", "name", "ride"};
            String prefix = args[0].toLowerCase();
            for (String s : cmds) {
                if (s.toLowerCase().startsWith(prefix))
                    options.add(s);
            }
            return options;
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("sv"))) {
            List<String> types = new ArrayList<>();
            String prefix = args[1].toLowerCase();
            for (EntityType et : EntityType.values()) {
                String name = et.getName();
                if (name != null && name.toLowerCase().startsWith(prefix))
                    types.add(name);
            }
            return types;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("ride")) {
            List<String> d = new ArrayList<>();
            d.add("-d");
            return d;
        } else return Collections.emptyList();
    }

}
