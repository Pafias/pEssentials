package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.CC;
import me.pafias.putils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.*;
import java.util.stream.Stream;

public class EntityCommand extends ICommand {

    public EntityCommand() {
        super("entity", "essentials.entity", "Entity utils", "/e", "e");
    }

    private static final int ENTITY_RESOLVE_RANGE = 5;

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        final Player player = (Player) sender;
        if (args.length == 0) {
            help(player, label);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "spawn", "s", "sv" -> handleSpawn(player, args);
            case "delete", "d", "dv", "remove" -> handleDelete(player);
            case "move" -> handleMove(player, args);
            case "gravity" -> handleGravity(player);
            case "age" -> handleAge(player, args);
            case "teleport", "tp" -> handleTeleport(player, label, args);
            case "god", "invulnerable", "invulnerability" -> handleGod(player);
            case "glow", "glowing" -> handleGlow(player);
            case "name", "customname" -> handleName(player, args);
            case "ride", "r" -> handleRide(player, args);
            case "invis", "invisible", "invisibility" -> handleInvisibility(player);
            case "ai", "aware", "awareness" -> handleAi(player);
            case "owner" -> handleOwner(player, args);
            case "target" -> handleTarget(player, args);
            case "pose" -> handlePose(player, args);
            default -> help(player, label);
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            final String arg = args[0].toLowerCase();
            return Stream.of("spawn", "delete", "fly", "gravity", "age", "tp", "god", "glow", "name", "ride", "invis", "ai", "owner", "target", "pose")
                    .filter(s -> s.toLowerCase().startsWith(arg))
                    .toList();
        }
        if (args.length == 2 && isAny(args[0], "spawn", "s", "sv")) {
            final String arg = args[1].toLowerCase();
            final List<String> list = new ArrayList<>();
            for (EntityType type : EntityType.values()) {
                if (type.getName() == null) continue;
                final String name = type.getName().toLowerCase();
                if (name.startsWith(arg)) {
                    list.add(name);
                }
            }
            return list;
        }
        if (args.length == 2 && isAny(args[0], "ride", "r")) {
            return List.of("-d", "-p");
        }
        if (args.length == 3 && isAny(args[0], "spawn", "s", "sv")) {
            return Collections.singletonList("-d");
        }
        if (args.length == 2 && isAny(args[0], "move", "tp", "owner", "target")) {
            return RandomUtils.tabCompletePlayers(sender, args[1]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("pose")) {
            final String arg = args[1].toLowerCase();
            final List<String> list = new ArrayList<>();
            for (Pose pose : Pose.values()) {
                final String name = pose.name().toLowerCase();
                if (name.startsWith(arg)) {
                    list.add(name);
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    private boolean isAny(String value, String... options) {
        for (String option : options) {
            if (option.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private void help(Player player, String label) {
        player.sendMessage(CC.t("&2/" + label + " spawn [type] [-d] &7- Spawns an entity with you on it, or not (-d)"));
        player.sendMessage(CC.t("&2/" + label + " delete &7- Deletes an entity"));
        player.sendMessage(CC.t("&2/" + label + " move [player] &7- Toggle fly/move mode for an entity"));
        player.sendMessage(CC.t("&2/" + label + " gravity &7- Toggle gravity mode for an entity"));
        player.sendMessage(CC.t("&2/" + label + " age [age] &7- Change the age of an entity"));
        player.sendMessage(CC.t("&2/" + label + " tp <player> &7- Teleport together with your entity to someone"));
        player.sendMessage(CC.t("&2/" + label + " god &7- Toggle invulnerability for an entity"));
        player.sendMessage(CC.t("&2/" + label + " glow &7- Toggle glowing for an entity"));
        player.sendMessage(CC.t("&2/" + label + " name [name] &7- Change the name of an entity"));
        player.sendMessage(CC.t("&2/" + label + " ride [-d] [-p] &7- Ride on (or dismount -d) an entity (-p excludes players)"));
        player.sendMessage(CC.t("&2/" + label + " invis &7- Toggle invisibility for an entity"));
        player.sendMessage(CC.t("&2/" + label + " ai &7- Toggle AI on an entity"));
        player.sendMessage(CC.t("&2/" + label + " owner [player] &7- Set the owner of an entity"));
        player.sendMessage(CC.t("&2/" + label + " target [player] &7- Set the target of an entity"));
        player.sendMessage(CC.t("&2/" + label + " pose [pose] &7- Set the pose of an entity"));
    }

    private void handleSpawn(Player player, String[] args) {
        final EntityType type = args.length > 1 ? EntityType.fromName(args[1]) : EntityType.HORSE;
        if (type == null) {
            player.sendMessage(CC.t("&cInvalid entity!"));
            return;
        }
        player.getWorld().spawnEntity(player.getLocation(), type, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
            if (entity == null) {
                player.sendMessage(CC.t("&cFailed to spawn entity!"));
                return;
            }
            if (entity instanceof Tameable tameable) {
                tameable.setTamed(true);
                tameable.setOwner(player);
            }
            if (entity instanceof Ageable ageable) {
                ageable.setAdult();
            }
            if (entity instanceof InventoryHolder inventoryHolder) {
                inventoryHolder.getInventory().setItem(0, new ItemStack(Material.SADDLE, 1));
            }
            if (!Arrays.asList(args).contains("-d")) {
                entity.addPassenger(player);
            }
            player.sendActionBar(CC.t("&6Spawned: &b" + entity));
        });
    }

    private void handleDelete(Player player) {
        final Entity entity = resolveEntity(player, true);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entities found nearby!"));
            return;
        }
        entity.remove();
        player.sendActionBar(CC.t("&6Removed: &r" + entity));
    }

    private void handleMove(Player player, String[] args) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("packetevents")) {
            player.sendMessage(CC.t("&cPacketEvents not found! This feature only works with PacketEvents!"));
            return;
        }
        final Entity entity;
        if (args.length >= 2) {
            final Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
            entity = target.getVehicle();
        } else {
            entity = player.getVehicle();
        }
        if (entity == null) {
            player.sendMessage(CC.t("&cNot on an entity!"));
            return;
        }
        final User user = plugin.getSM().getUserManager().getUser(player);
        user.movingEntity = !user.movingEntity;
        entity.setGravity(!user.movingEntity);
        player.sendActionBar(CC.t("&6Entity move: " + (user.movingEntity ? "&aON" : "&cOFF")));
    }

    private void handleGravity(Player player) {
        final Entity entity = resolveEntity(player, false);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        entity.setGravity(!entity.hasGravity());
        player.sendActionBar(CC.t("&6Entity gravity: &r" + (entity.hasGravity() ? "&aON" : "&cOFF")));
    }

    private void handleAge(Player player, String[] args) {
        final Entity entity = resolveEntity(player, true);
        if (entity == null) {
            return;
        }
        if (!(entity instanceof Ageable ageable)) {
            player.sendMessage(CC.t("&cThat's not possible with this (type of) entity."));
            return;
        }
        if (args.length >= 2) {
            try {
                ageable.setAge(Integer.parseInt(args[1]));
            } catch (NumberFormatException ex) {
                player.sendMessage(CC.t("&cInvalid age."));
            }
        } else if (ageable.isAdult()) {
            ageable.setBaby();
        } else {
            ageable.setAdult();
        }
    }

    private void handleTeleport(Player player, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(CC.t("&c/" + label + " " + args[0] + " <player>"));
            return;
        }
        final Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        final Entity entity = player.getVehicle();
        if (entity == null) {
            player.sendMessage(CC.t("&cYou are not on an entity!"));
            return;
        }
        final Collection<Entity> riding = entity.getPassengers();
        entity.teleportAsync(target.getLocation()).thenAccept(success -> {
            if (success) {
                Tasks.runLaterSync(2, () -> {
                    for (Entity e : riding) {
                        entity.addPassenger(e);
                    }
                });
            } else {
                player.sendMessage(CC.t("&cFailed to teleport."));
            }
        });
    }

    private void handleGod(Player player) {
        final Entity entity = resolveEntity(player, false);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        entity.setInvulnerable(!entity.isInvulnerable());
        player.sendActionBar(CC.t("&6Entity invulnerability: &r" + (entity.isInvulnerable() ? "&aON" : "&cOFF")));
    }

    private void handleGlow(Player player) {
        final Entity entity = resolveEntity(player, false);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        entity.setGlowing(!entity.isGlowing());
        player.sendActionBar(CC.t("&6Entity glowing: &r" + (entity.isGlowing() ? "&aON" : "&cOFF")));
    }

    private void handleName(Player player, String[] args) {
        final Entity entity = resolveEntity(player, true);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        if (args.length < 2) {
            entity.setCustomNameVisible(!entity.isCustomNameVisible());
            player.sendActionBar(CC.t("&6Entity name visibility: " + (entity.isCustomNameVisible() ? "&aVISIBLE" : "&cINVISIBLE")));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(i == args.length - 1 ? "" : " ");
        }
        entity.setCustomNameVisible(true);
        try {
            entity.customName(CC.a(sb.toString()));
            player.sendActionBar(CC.a("&6Entity name: &r").append(entity.customName()));
        } catch (Throwable ex) {
            entity.setCustomName(CC.t(sb.toString()));
            player.sendActionBar(CC.t("&6Entity name: &r" + entity.getCustomName()));
        }
    }

    private void handleRide(Player player, String[] args) {
        if (args.length == 2 && args[1].equalsIgnoreCase("-d")) {
            if (!player.isInsideVehicle()) {
                player.sendMessage(CC.t("&cYou are not riding anything."));
                return;
            }
            player.leaveVehicle();
            return;
        }
        final Entity entity = resolveEntity(player, Arrays.asList(args).contains("-p"));
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        entity.addPassenger(player);
        player.sendActionBar(CC.t("&6Now riding: &b" + entity));
    }

    private void handleInvisibility(Player player) {
        final Entity entity = resolveEntity(player, false);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        if (!(entity instanceof LivingEntity livingEntity)) {
            player.sendMessage(CC.t("&cThis (type of) entity cannot become invisible."));
            return;
        }
        livingEntity.setInvisible(!livingEntity.isInvisible());
        player.sendActionBar(CC.t("&6Entity invisibility: " + (livingEntity.isInvisible() ? "&7INVISIBLE" : "&bVISIBLE")));
    }

    private void handleAi(Player player) {
        final Entity entity = resolveEntity(player, true);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        Boolean aware = null;
        Boolean ai = null;
        if (entity instanceof Mob mob) {
            mob.setAware(!mob.isAware());
            aware = mob.isAware();
        }
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setAI(!livingEntity.hasAI());
            ai = livingEntity.hasAI();
        }
        player.sendActionBar(CC.t("&6Entity AI toggled: " +
                (aware != null ? (aware ? "&aAWARE" : "&cUNAWARE") : "")
                + (ai != null ? ", " + (ai ? "&aAI ON" : "&cAI OFF") : "")
        ));
    }

    private void handleOwner(Player player, String[] args) {
        final Entity entity = resolveEntity(player, true);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        if (!(entity instanceof Tameable tameable)) {
            player.sendMessage(CC.t("&cThis (type of) entity cannot have an owner."));
            return;
        }
        final Player owner;
        if (args.length >= 2) {
            owner = plugin.getServer().getPlayer(args[1]);
            if (owner == null) {
                player.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
        } else {
            owner = null;
        }
        tameable.setOwner(owner);
        if (owner == null) {
            player.sendActionBar(CC.t("&6Entity owner removed."));
        } else {
            player.sendActionBar(CC.t("&6Entity owner set to " + owner.getName() + "."));
        }
    }

    private void handleTarget(Player player, String[] args) {
        final Entity entity = resolveEntity(player, true);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        if (!(entity instanceof Mob mob)) {
            player.sendMessage(CC.t("&cThis (type of) entity cannot have a target."));
            return;
        }
        final Player target;
        if (args.length >= 2) {
            target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }
        } else {
            target = null;
        }
        mob.setTarget(target);
        if (target == null) {
            player.sendActionBar(CC.t("&6Entity target removed."));
        } else {
            player.sendActionBar(CC.t("&6Entity target set to " + target.getName() + "."));
        }
    }

    private void handlePose(Player player, String[] args) {
        final Entity entity = resolveEntity(player, false);
        if (entity == null) {
            player.sendMessage(CC.t("&cNo entity found."));
            return;
        }
        Pose pose = null;
        if (args.length >= 2) {
            final String arg = args[1].toLowerCase();
            for (Pose p : Pose.values()) {
                final String name = p.name().toLowerCase();
                if (name.startsWith(arg)) {
                    pose = p;
                    break;
                }
            }
            if (pose == null) {
                player.sendMessage(CC.t("&cInvalid pose!"));
                return;
            }
            entity.setPose(pose, true);
            player.sendActionBar(CC.t("&6Entity pose set to &b" + pose.name()));
        } else {
            entity.setPose(Pose.STANDING, false);
            player.sendActionBar(CC.t("&6Entity pose reset."));
        }
    }

    private Entity resolveEntity(Player player, boolean ignorePlayers) {
        if (player.isInsideVehicle())
            return player.getVehicle();

        final RayTraceResult raytrace = player.rayTraceEntities(ENTITY_RESOLVE_RANGE, true);
        if (raytrace != null) {
            final Entity traced = raytrace.getHitEntity();
            if (traced != null && (!ignorePlayers || !(traced instanceof Player)))
                return traced;
        }

        for (Entity nearby : player.getNearbyEntities(ENTITY_RESOLVE_RANGE, ENTITY_RESOLVE_RANGE, ENTITY_RESOLVE_RANGE)) {
            if (ignorePlayers && nearby instanceof Player)
                continue;
            return nearby;
        }

        return null;
    }

}
