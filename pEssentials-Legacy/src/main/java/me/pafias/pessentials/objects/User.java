package me.pafias.pessentials.objects;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.Setter;
import me.pafias.pessentials.pEssentials;
import me.pafias.pessentials.services.FreezeManager;
import me.pafias.pessentials.services.VanishManager;
import me.pafias.pessentials.util.Reflection;
import me.pafias.putils.CC;
import me.pafias.putils.LCC;
import me.pafias.putils.Tasks;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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

    public boolean flyingEntity, movingEntity;
    public Location lastLocation;

    @Getter
    @Setter
    private boolean inStaffchat;

    private final GameProfile profile;
    private GameProfile newIdentity;
    private BukkitTask idTask;

    @Getter
    private BukkitTask freezeTask;

    @Setter
    @Getter
    private boolean blockingPMs;

    @Getter
    private final Set<UUID> blocking = new HashSet<>();

    public User(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();

        profile = Reflection.getGameProfile(player);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        if (newIdentity != null) return newIdentity.getName();
        return profile.getName();
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public void message(boolean colorize, String content) {
        if (colorize)
            try {
                player.sendMessage(CC.a(content));
            } catch (Throwable ex) {
                player.sendMessage(LCC.t(content));
            }
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
            idTask = Tasks.runRepeatingSync(2, 40, () -> {
                Reflection.sendActionbar(player, LCC.t(String.format("&aCurrently disguised. &6Name: &b%s", getName())));
            });
        }
        Reflection.setGameProfile(player, profile);
        hideAndShow();
        player.setDisplayName(profile.getName());
    }

    private void hideAndShow() {
        Tasks.runSync(() -> plugin.getServer().getOnlinePlayers().forEach(p -> p.hidePlayer(player)));
        Tasks.runLaterSync(2, () -> plugin.getServer().getOnlinePlayers().forEach(p -> p.showPlayer(player)));
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
            player.sendActionBar(LCC.t("&c&lYou are frozen! Do not log out."));
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

}
