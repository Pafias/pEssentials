package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EntityCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public EntityCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.entity")) {
            final Player player = (Player) sender;
            User user = plugin.getSM().getUserManager().getUser(player);
            if (args.length == 0) {
                sender.sendMessage(CC.t("&2/" + label + " s [type] &f- Spawns an entity with you on it"));
                sender.sendMessage(CC.t("&2/" + label + " d &f- Deletes the entity you're on or else the closest one to you"));
                sender.sendMessage(CC.t("&2/" + label + " fly [player] &f- Toggle fly mode for the entity where you/player is sitting on"));
                sender.sendMessage(CC.t("&2/" + label + " gravity &f- Toggle gravity mode for the entity where you're sitting on"));
                sender.sendMessage(CC.t("&2/" + label + " age [age] &f- Change the age of the entity where you're sitting on"));
                sender.sendMessage(CC.t("&2/" + label + " tp <player> &f- Teleport together with your entity to someone"));
                sender.sendMessage(CC.t("&2/" + label + " god &f- Toggle god mode for the entity where you're sitting on"));
                sender.sendMessage(CC.t("&2/" + label + " glow &f- Toggle glowing for the entity where you're sitting on"));
                sender.sendMessage(CC.t("&2/" + label + " name [name] &f- Change the name of the entity you're sitting on"));
                sender.sendMessage(CC.t("&2/" + label + " ride [-d] &f- Ride on (or dismount -d) the closest entity to you"));
                sender.sendMessage(CC.t("&2/" + label + " invis &f- Toggle invisibility for the entity where you're sitting on or else the closest one to you"));
                sender.sendMessage(CC.t("&2/" + label + " ai &f- Toggle AI on the entity you're sitting on"));
                return true;
            }
            if (args[0].equalsIgnoreCase("fly") && sender.hasPermission("essentials.entity.fly")) {
                if (!plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
                    sender.sendMessage(CC.t("&cProtocolLib not found! This feature only works with ProtocolLib!"));
                    return true;
                }
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                entity.setGravity(!entity.hasGravity());
                user.flyingEntity = !user.flyingEntity;
                entity.setInvulnerable(!entity.isInvulnerable());
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity fly: " + (user.flyingEntity ? "&aON" : "&cOFF")));
            } else if (args[0].equalsIgnoreCase("move") && sender.hasPermission("essentials.entity.move")) {
                if (!plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
                    sender.sendMessage(CC.t("&cProtocolLib not found! This feature only works with ProtocolLib!"));
                    return true;
                }
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                entity.setGravity(!entity.hasGravity());
                user.movingEntity = !user.movingEntity;
                entity.setInvulnerable(!entity.isInvulnerable());
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity move: " + (user.movingEntity ? "&aON" : "&cOFF")));
            } else if ((args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("sv")) && sender.hasPermission("essentials.entity.spawn")) {
                Entity entity;
                if (args.length > 1) {
                    entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.fromName(args[1]));
                } else {
                    entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
                }
                if (entity == null) {
                    sender.sendMessage(CC.t("&cInvalid entity!"));
                    return true;
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
                Entity entity;
                if (player.isInsideVehicle()) {
                    entity = player.getVehicle();
                } else {
                    try {
                        entity = player.getNearbyEntities(5.0D, 5.0D, 5.0D).stream().filter(e -> !(e instanceof Player)).collect(Collectors.toList()).get(0);
                    } catch (IndexOutOfBoundsException ex) {
                        sender.sendMessage(CC.t("&cNo entities found nearby!"));
                        return true;
                    }
                }
                if (entity == null) {
                    sender.sendMessage(CC.t("&cNo entities found nearby!"));
                    return true;
                }
                entity.remove();
                user.flyingEntity = false;
                user.movingEntity = false;
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Removed: &r" + entity));
            } else if (args[0].equalsIgnoreCase("gravity") && sender.hasPermission("essentials.entity.gravity")) {
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                entity.setGravity(!entity.hasGravity());
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity gravity: &r" + (entity.hasGravity() ? "&aON" : "&cOFF")));
            } else if (args[0].equalsIgnoreCase("age") && sender.hasPermission("essentials.entity.age")) {
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                if (!(entity instanceof Ageable)) {
                    sender.sendMessage(CC.t("&cThat's not possible with this entity."));
                    return true;
                }
                if (args.length >= 2) {
                    try {
                        ((Ageable) entity).setAge(Integer.parseInt(args[1]));
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(CC.t("&cInvalid age."));
                        return true;
                    }
                } else if (((Ageable) entity).isAdult()) {
                    ((Ageable) entity).setBaby();
                } else {
                    ((Ageable) entity).setAdult();
                }
            } else if ((args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) && sender.hasPermission("essentials.entity.teleport")) {
                if (args.length < 2) {
                    sender.sendMessage(CC.t("&c/" + label + " " + args[0] + " <player>"));
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(CC.t("&cPlayer not found!"));
                    return true;
                }
                Entity entity = player.getVehicle();
                if (entity == null)
                    entity = player;
                player.teleport(target);
                if (entity != player) {
                    entity.teleport(target);
                    Entity finalEntity = entity;
                    new BukkitRunnable() {
                        public void run() {
                            finalEntity.setPassenger(player);
                        }
                    }.runTaskLater(plugin, 40L);
                }
            } else if (args[0].equalsIgnoreCase("god") && sender.hasPermission("essentials.entity.god")) {
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                entity.setInvulnerable(!entity.isInvulnerable());
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity invulnerability: &r" + (entity.isInvulnerable() ? "&aON" : "&cOFF")));
            } else if ((args[0].equalsIgnoreCase("glow") || args[0].equalsIgnoreCase("glowing")) && sender.hasPermission("essentials.entity.glow")) {
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                entity.setGlowing(!entity.isGlowing());
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity glowing: &r" + (entity.isGlowing() ? "&aON" : "&cOFF")));
            } else if (args[0].equalsIgnoreCase("name") && sender.hasPermission("essentials.entity.name")) {
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                if (args.length < 2) {
                    entity.setCustomNameVisible(!entity.isCustomNameVisible());
                    plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity name visibility: " + (entity.isCustomNameVisible() ? "&aVISIBLE" : "&cINVISIBLE")));
                    return true;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++)
                    sb.append(args[i]).append(i == args.length - 1 ? "" : " ");
                entity.setCustomNameVisible(true);
                entity.setCustomName(CC.t(sb.toString()));
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity name: &r" + entity.getCustomName()));
            } else if (args[0].equalsIgnoreCase("ride") && sender.hasPermission("essentials.entity.ride")) {
                if (args.length == 2 && args[1].equalsIgnoreCase("-d")) {
                    if (!player.isInsideVehicle()) {
                        sender.sendMessage(CC.t("&cYou are not riding anything."));
                        return true;
                    }
                    player.leaveVehicle();
                    return true;
                }
                Entity entity;
                try {
                    List<Entity> list = player.getNearbyEntities(2.5D, 2.5D, 2.5D);
                    entity = list.get(new Random().nextInt(list.size()));
                } catch (ArrayIndexOutOfBoundsException ex) {
                    sender.sendMessage(CC.t("&cNo entities found nearby."));
                    return true;
                }
                entity.setPassenger(player);
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Now riding: &b" + entity));
            } else if (args[0].equalsIgnoreCase("invis") || args[0].equalsIgnoreCase("invisible") || args[0].equalsIgnoreCase("invisibility")) {
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                if (!(entity instanceof LivingEntity)) {
                    sender.sendMessage(CC.t("&cThis entity cannot become invisible."));
                    return true;
                }
                plugin.getSM().getNMSProvider().toggleInvisibility((LivingEntity) entity);
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity invisibility: " + (plugin.getSM().getNMSProvider().isInvisible((LivingEntity) entity) ? "&7INVISIBLE" : "&bVISIBLE")));
            } else if (args[0].equalsIgnoreCase("ai")) {
                Entity entity = player.getVehicle();
                if (entity == null) {
                    sender.sendMessage(CC.t("&cYou're not on an entity!"));
                    return true;
                }
                try {
                    if (entity instanceof Mob)
                        ((Mob) entity).setAware(!((Mob) entity).isAware());
                } catch (Exception ignored) {
                }
                try {
                    if (entity instanceof LivingEntity)
                        ((LivingEntity) entity).setAI(!((LivingEntity) entity).hasAI());
                } catch (Exception ignored) {
                    sender.sendMessage(CC.t("&6This entity does not have AI"));
                    return true;
                }
                plugin.getSM().getNMSProvider().sendActionbar(user.getPlayer(), CC.t("&6Entity AI toggled."));
            }
        }
        return true;
    }

}
