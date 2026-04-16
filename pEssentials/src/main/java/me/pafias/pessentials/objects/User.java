package me.pafias.pessentials.objects;

import com.destroystokyo.paper.profile.PlayerProfile;
import lombok.Getter;
import lombok.Setter;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.FreezeManager;
import me.pafias.pessentials.services.VanishManager;
import me.pafias.putils.CC;
import me.pafias.putils.Tasks;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User implements Messageable {

    private final pEssentials plugin = pEssentials.get();
    private final VanishManager vanishManager = plugin.getSM().getVanishManager();
    private final FreezeManager freezeManager = plugin.getSM().getFreezeManager();


    @Getter
    private final Player player;
    private final UUID uuid;

    @Getter
    private final PersistentDataContainer dataContainer;

    public boolean movingEntity;
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
    private static final NamespacedKey BLOCKING_KEY =
            new NamespacedKey(pEssentials.get(), "blocking");

    public User(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();

        dataContainer = player.getPersistentDataContainer();

        String blockingData = dataContainer.getOrDefault(BLOCKING_KEY, PersistentDataType.STRING, "");
        if (!blockingData.isBlank()) {
            for (String blocked : blockingData.split(","))
                blocking.add(UUID.fromString(blocked));
        }

        profile = player.getPlayerProfile();
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    public String getName() {
        return newIdentity != null ? newIdentity.getName() : profile.getName();
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
        if (!(sender instanceof User user)) return false;
        return blocking.contains(user.getUUID());
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
                player.sendActionBar(CC.t("&aCurrently disguised."));
            });
        }
        player.setPlayerProfile(profile);
        player.setDisplayName(profile.getName());
    }

    public void crash() {
        try {
            final Location location = player.getEyeLocation();
            for (int i = 0; i < 5; i++)
                player.spawnParticle(Particle.CRIT, location.getX(), location.getY(), location.getZ(), Integer.MAX_VALUE);
        } catch (Throwable e) {
            throw new IllegalStateException("This server version does not support this feature!", e);
        }
    }

    public boolean isVanished() {
        return vanishManager.isVanished(player);
    }

    public boolean isFrozen() {
        return freezeManager.getFrozenUsers().contains(player.getUniqueId());
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
            freezeManager.removeFrozen(player);
            return;
        }

        freezeManager.applyFrozen(player);
        freezeTask = Tasks.runRepeatingSync(0, 40, () -> {
            player.sendActionBar(CC.t("&c&lYou are frozen! Do not log out."));
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
        return blocking;
    }

    public void addBlocking(UUID uuid) {
        blocking.add(uuid);

        final StringBuilder sb = new StringBuilder();
        for (final UUID id : blocking) {
            if (sb.length() > 0) sb.append(",");
            sb.append(id);
        }
        final String blockingData = sb.toString();

        dataContainer.set(BLOCKING_KEY, PersistentDataType.STRING, blockingData);
    }

    public void removeBlocking(UUID uuid) {
        blocking.remove(uuid);

        if (!blocking.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (final UUID id : blocking) {
                if (sb.length() > 0) sb.append(",");
                sb.append(id);
            }
            final String blockingData = sb.toString();

            dataContainer.set(BLOCKING_KEY, PersistentDataType.STRING, blockingData);
        } else {
            dataContainer.remove(BLOCKING_KEY);
        }
    }

}
