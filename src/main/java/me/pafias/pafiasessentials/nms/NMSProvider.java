package me.pafias.pafiasessentials.nms;

import com.mojang.authlib.GameProfile;
import me.pafias.pafiasessentials.PafiasEssentials;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public interface NMSProvider {

    PafiasEssentials plugin = PafiasEssentials.get();

    int getPing(Player player);

    void setGameProfile(Player player, GameProfile profile);

    GameProfile getGameProfile(Player player);

    void sendActionbar(Player player, String text);

    boolean isInvisible(LivingEntity entity);

    void toggleInvisibility(LivingEntity entity);

    ItemStack getSkull();

    void crash(Player player);

    void sendCustomPayload(Player player, String channel, byte[] bytes);

    void sendParticle(Player player, String particle, double x, double y, double z, int amount);

    void playSound(Player player, Sound sound, double x, double y, double z, float volume, float pitch);

    void rickroll(Player player, @Nullable CommandSender sender);

    void handleRickrollMove(PlayerMoveEvent event);

    void handleRickrollQuit(PlayerQuitEvent event);

    boolean steeringForward(Object packet);

    boolean steeringBackwards(Object packet);

    boolean steeringLeft(Object packet);

    boolean steeringRight(Object packet);

    boolean steeringPressingSpace(Object packet);

    boolean steeringPressingShift(Object packet);

}