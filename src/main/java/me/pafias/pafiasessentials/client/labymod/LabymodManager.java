package me.pafias.pafiasessentials.client.labymod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.client.labymod.objects.LabymodUser;
import me.pafias.pafiasessentials.objects.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LabymodManager implements PluginMessageListener {

    private final PafiasEssentials plugin;

    public LabymodManager(PafiasEssentials plugin) {
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "labymod3:main", this);
    }

    private Set<LabymodUser> labymodUsers = new HashSet<>();

    public boolean usingLabymod(User user) {
        return labymodUsers.stream().anyMatch(u -> u.getUser().getUUID().equals(user.getUUID()));
    }

    public LabymodUser getLabymodUser(Player player) {
        return labymodUsers.stream().filter(user -> user.getUser().getUUID().equals(player.getUniqueId())).findAny().orElse(null);
    }

    public LabymodUser getLabymodUser(UUID uuid) {
        return labymodUsers.stream().filter(user -> user.getUser().getUUID().equals(uuid)).findAny().orElse(null);
    }

    public LabymodUser getLabymodUser(User user) {
        return labymodUsers.stream().filter(u -> u.getUser().equals(user)).findAny().orElse(null);
    }

    public void removeUser(Player player) {
        labymodUsers.remove(getLabymodUser(player));
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("labymod3:main")) return;
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        ByteBuf buf = Unpooled.wrappedBuffer(message);
        String key = LabyModProtocol.readString(buf, Short.MAX_VALUE);
        String json = LabyModProtocol.readString(buf, Short.MAX_VALUE);
        if (key.equals("INFO")) {
            LabymodUser user = new LabymodUser(player, new JsonParser().parse(json));
            labymodUsers.add(user);
            new BukkitRunnable() {
                @Override
                public void run() {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("hasGame", true);
                    jo.addProperty("game_mode", plugin.getSM().getVariables().serverName);
                    jo.addProperty("game_startTime", System.currentTimeMillis());
                    jo.addProperty("game_endTime", 0);
                    user.sendLabyMessage(user.getUser().getPlayer(), "discord_rpc", jo);
                }
            }.runTaskLaterAsynchronously(plugin, (3 * 20));
        }
    }

}
