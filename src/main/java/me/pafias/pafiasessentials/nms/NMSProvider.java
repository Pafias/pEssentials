package me.pafias.pafiasessentials.nms;

import com.mojang.authlib.GameProfile;
import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSProvider {

    PafiasEssentials plugin = PafiasEssentials.get();

    void setGameProfile(Player player, GameProfile profile);
    GameProfile getGameProfile(Player player);

    boolean isInvisible(LivingEntity entity);
    void toggleInvisibility(LivingEntity entity);

    ItemStack getSkull();

    void sendParticle(Player player, String particle, double x, double y, double z, int amount);

    void playSound(Player player, Sound sound, double x, double y, double z, float volume, float pitch);

    boolean steeringForward(Object packet);

    boolean steeringBackwards(Object packet);

    boolean steeringLeft(Object packet);

    boolean steeringRight(Object packet);

    boolean steeringPressingSpace(Object packet);

    boolean steeringPressingShift(Object packet);

}