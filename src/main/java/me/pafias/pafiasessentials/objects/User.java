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

    private final PafiasEssentials plugin = PafiasEssentials.get();

    private final Player player;
    public boolean flyingEntity, movingEntity;
    public Location lastLocation;
    private boolean inStaffchat;
    private final GameProfile profile;
    private GameProfile newIdentity;
    private BukkitTask idTask;

    public User(Player player) {
        this.player = player;
        profile = plugin.getSM().getNMSProvider().getGameProfile(player);
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
                    plugin.getSM().getNMSProvider().sendActionbar(player, CC.t(String.format("&aCurrently disguised. &6Name: &b%s", getName())));
                }
            }.runTaskTimer(plugin, 2, 40);
        }
        plugin.getSM().getNMSProvider().setGameProfile(player, profile);
        hideAndShow();
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

    public boolean isVanished() {
        return plugin.getSM().getVanishManager().isVanished(player);
    }

    public boolean isFrozen() {
        return plugin.getSM().getFreezeManager().getFrozenUsers().contains(player.getUniqueId());
    }

    public void setFrozen(boolean frozen) {
        if (frozen)
            plugin.getSM().getFreezeManager().getFrozenUsers().remove(player.getUniqueId());
        else
            plugin.getSM().getFreezeManager().getFrozenUsers().add(player.getUniqueId());
    }

}
