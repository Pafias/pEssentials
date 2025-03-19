package me.pafias.pessentials.objects;

import com.mojang.authlib.GameProfile;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Reflection;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

    private final pEssentials plugin = pEssentials.get();

    private final Player player;
    public boolean flyingEntity, movingEntity;
    public Location lastLocation;
    private boolean inStaffchat;
    private final GameProfile profile;
    private GameProfile newIdentity;
    private BukkitTask idTask;
    private boolean blockingPMs;
    private Set<UUID> blocking = new HashSet<>();

    public User(Player player) {
        this.player = player;
        profile = Reflection.getGameProfile(player);
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    public String getName() {
        if (newIdentity != null) return newIdentity.getName();
        return profile.getName();
    }

    public String getRealName() {
        return profile.getName();
    }

    public boolean isInStaffChat() {
        return inStaffchat;
    }

    public void setInStaffchat(boolean inStaffChat) {
        this.inStaffchat = inStaffChat;
    }

    public GameProfile getOriginalGameProfile() {
        return profile;
    }

    public boolean hasIdentity() {
        return newIdentity != null;
    }

    public void setIdentity(GameProfile profile) {
        if (profile == this.profile) {
            this.newIdentity = null;
            if (idTask != null)
                idTask.cancel();
            idTask = null;
        } else {
            this.newIdentity = profile;
            if (idTask != null)
                idTask.cancel();
            idTask = new BukkitRunnable() {
                @Override
                public void run() {
                    Reflection.sendActionbar(player, CC.t(String.format("&aCurrently disguised. &6Name: &b%s", getName())));
                }
            }.runTaskTimer(plugin, 2, 40);
        }
        Reflection.setGameProfile(player, profile);
        hideAndShow();
        player.setDisplayName(profile.getName());
    }

    private void hideAndShow() {
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getServer().getOnlinePlayers().forEach(p -> p.hidePlayer(player));
            }
        }.runTask(plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getServer().getOnlinePlayers().forEach(p -> p.showPlayer(player));
            }
        }.runTaskLater(plugin, 2);
    }

    public void crash() {
        for (int i = 0; i < 5; i++)
            player.spawnParticle(Particle.CRIT, player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ(), Integer.MAX_VALUE);
    }

    public boolean isVanished() {
        return plugin.getSM().getVanishManager().isVanished(player);
    }

    public boolean isFrozen() {
        return plugin.getSM().getFreezeManager().getFrozenUsers().contains(player.getUniqueId());
    }

    public void setFrozen(boolean frozen) {
        if (frozen)
            plugin.getSM().getFreezeManager().getFrozenUsers().add(player.getUniqueId());
        else
            plugin.getSM().getFreezeManager().getFrozenUsers().remove(player.getUniqueId());
    }

    public boolean isBlockingPMs() {
        return blockingPMs;
    }

    public void setBlockingPMs(boolean blockingPMs) {
        this.blockingPMs = blockingPMs;
    }

    public Set<UUID> getBlocking() {
        return blocking;
    }

}
