package me.pafias.pafiasessentials.objects;

import com.mojang.authlib.GameProfile;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class User {

    private final Player player;
    public boolean flyingEntity, movingEntity;
    public Location lastLocation;
    private boolean inStaffchat;
    private final GameProfile profile;
    private GameProfile newIdentity;
    private BukkitTask idTask;

    public User(Player player) {
        this.player = player;
        profile = PafiasEssentials.get().getSM().getNMSProvider().getGameProfile(player);
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
                    player.sendActionBar(CC.t(String.format("&aCurrently disguised. &6Name: &b%s", getName())));
                }
            }.runTaskTimer(PafiasEssentials.get(), 2, 40);
        }
        PafiasEssentials.get().getSM().getNMSProvider().setGameProfile(player, profile);
        hideAndShow();
    }

    private void hideAndShow() {
        new BukkitRunnable() {
            @Override
            public void run() {
                PafiasEssentials.get().getServer().getOnlinePlayers().forEach(p -> p.hidePlayer(player));
            }
        }.runTask(PafiasEssentials.get());
        new BukkitRunnable() {
            @Override
            public void run() {
                PafiasEssentials.get().getServer().getOnlinePlayers().forEach(p -> p.showPlayer(player));
            }
        }.runTaskLater(PafiasEssentials.get(), 5);
    }

    public boolean isVanished() {
        return PafiasEssentials.get().getSM().getVanishManager().isVanished(player);
    }

}
