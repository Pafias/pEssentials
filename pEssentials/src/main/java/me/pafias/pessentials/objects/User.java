package me.pafias.pessentials.objects;

import com.destroystokyo.paper.profile.PlayerProfile;
import lombok.Getter;
import lombok.Setter;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.putils.Tasks;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class User implements Messageable {

    private final pEssentials plugin = pEssentials.get();

    @Getter
    private final Player player;

    @Getter
    private final PersistentDataContainer dataContainer;

    public boolean flyingEntity, movingEntity;
    public Location lastLocation;

    @Getter
    @Setter
    private boolean inStaffchat;

    private final PlayerProfile profile;
    private PlayerProfile newIdentity;
    private BukkitTask idTask;

    private BukkitTask freezeTask;

    @Setter
    @Getter
    private boolean blockingPMs;

    private final Set<UUID> blocking = new HashSet<>();

    public User(Player player) {
        this.player = player;

        dataContainer = player.getPersistentDataContainer();

        String blockingData = dataContainer.getOrDefault(new NamespacedKey(plugin, "blocking"), PersistentDataType.STRING, "");
        if (!blockingData.isBlank()) {
            for (String blocked : blockingData.split(","))
                blocking.add(UUID.fromString(blocked));
        }

        profile = player.getPlayerProfile();
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    @Override
    public boolean isOnline() {
        return player != null && player.isOnline();
    }

    public String getName() {
        if (newIdentity != null) return newIdentity.getName();
        return profile.getName();
    }

    @Override
    public void message(boolean colorize, String content) {
        if (colorize)
            player.sendMessage(CC.a(content));
        else
            player.sendMessage(content);
    }

    @Override
    public boolean isBlockingPMsFrom(Messageable sender) {
        if (sender instanceof ConsoleUser) return false;
        return blocking.contains(((User) sender).getUUID());
    }

    public String getRealName() {
        return profile.getName();
    }

    public PlayerProfile getOriginalGameProfile() {
        return profile;
    }

    public boolean hasIdentity() {
        return newIdentity != null;
    }

    public void setIdentity(PlayerProfile profile) {
        if (profile == this.profile) {
            this.newIdentity = null;
            if (idTask != null)
                idTask.cancel();
            idTask = null;
        } else {
            this.newIdentity = profile;
            if (idTask != null)
                idTask.cancel();
            idTask = Tasks.runRepeatingSync(2, 40, () -> {
                player.sendActionBar(CC.a("&aCurrently disguised."));
            });
        }
        player.setPlayerProfile(profile);
        player.setDisplayName(profile.getName());
    }

    public void crash() {
        try {
            for (int i = 0; i < 5; i++)
                player.spawnParticle(Particle.CRIT, player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ(), Integer.MAX_VALUE);
        } catch (Throwable e) {
            throw new IllegalStateException("This server version does not support this feature!", e);
        }
    }

    public boolean isVanished() {
        return plugin.getSM().getVanishManager().isVanished(player);
    }

    public boolean isFrozen() {
        return plugin.getSM().getFreezeManager().getFrozenUsers().contains(player.getUniqueId());
    }

    public void destroy() {
        if (idTask != null) {
            idTask.cancel();
            idTask = null;
        }
        if (freezeTask != null) {
            freezeTask.cancel();
            freezeTask = null;
        }
    }

    public void setFrozen(boolean frozen) {
        if (freezeTask != null) {
            freezeTask.cancel();
            freezeTask = null;
        }

        if (!frozen) {
            plugin.getSM().getFreezeManager().removeFrozen(player);
            return;
        }

        plugin.getSM().getFreezeManager().applyFrozen(player);
        freezeTask = Tasks.runRepeatingSync(0, 40, () -> {
            player.sendActionBar(CC.a("&c&lYou are frozen! Do not log out."));
        });
    }

    @Override
    public boolean canBypassBlock() {
        return player.hasPermission("essentials.block.bypass");
    }

    @Override
    public boolean canBypassMsgtoggle() {
        return player.hasPermission("essentials.msgtoggle.bypass");
    }

    @Unmodifiable
    public Set<UUID> getBlocking() {
        return Collections.unmodifiableSet(blocking);
    }

    public void addBlocking(UUID uuid) {
        blocking.add(uuid);

        String blockingData = blocking.stream().map(UUID::toString).collect(Collectors.joining(","));
        dataContainer.set(new NamespacedKey(plugin, "blocking"), PersistentDataType.STRING, blockingData);
    }

    public void removeBlocking(UUID uuid) {
        blocking.remove(uuid);

        if (!blocking.isEmpty()) {
            String blockingData = blocking.stream().map(UUID::toString).collect(Collectors.joining(","));
            dataContainer.set(new NamespacedKey(plugin, "blocking"), PersistentDataType.STRING, blockingData);
        } else {
            dataContainer.remove(new NamespacedKey(plugin, "blocking"));
        }
    }

}
