package me.pafias.pessentials.services;

import lombok.Getter;
import me.pafias.pessentials.objects.ConsoleUser;
import me.pafias.pessentials.objects.User;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class UserManager {

    @Getter
    private final ConsoleUser consoleUser = new ConsoleUser();

    @Getter
    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public User getUser(UUID uuid, Predicate<User> predicate) {
        User user = users.get(uuid);
        if (user != null && predicate.test(user))
            return user;
        return null;
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public User getUser(Player player, Predicate<User> predicate) {
        return getUser(player.getUniqueId(), predicate);
    }

    public User getUser(String name) {
        for (User user : users.values()) {
            if (user.getName().equalsIgnoreCase(name)
                    || user.getName().toLowerCase().startsWith(name.toLowerCase().trim()))
                return user;
        }
        return null;
    }

    public User getUser(String name, Predicate<User> predicate) {
        for (User user : users.values()) {
            if (user.getName().equalsIgnoreCase(name)
                    || user.getName().toLowerCase().startsWith(name.toLowerCase().trim())
                    && predicate.test(user))
                return user;
        }
        return null;
    }

    public void addUser(Player player) {
        users.put(player.getUniqueId(), new User(player));
    }

    public void removeUser(Player player) {
        users.remove(player.getUniqueId());
    }

    public void shutdown() {
        for (User user : users.values()) {
            user.setIdentity(user.getOriginalGameProfile());
        }
    }
}
