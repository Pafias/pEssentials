package me.pafias.pessentials.services;

import lombok.Getter;
import me.pafias.pessentials.objects.ConsoleUser;
import me.pafias.pessentials.objects.User;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class UserManager {

    @Getter
    private final ConsoleUser consoleUser = new ConsoleUser();

    @Getter
    private final Map<UUID, User> users = new HashMap<>();

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
        return users.values()
                .stream()
                .filter(u -> u.getName().equalsIgnoreCase(name) || u.getName().toLowerCase().startsWith(name.toLowerCase().trim()))
                .findFirst().orElse(null);
    }

    public User getUser(String name, Predicate<User> predicate) {
        return users.values()
                .stream()
                .filter(u -> (u.getName().equalsIgnoreCase(name) || u.getName().toLowerCase().startsWith(name.toLowerCase().trim())) && predicate.test(u))
                .findFirst().orElse(null);
    }

    public void addUser(Player player) {
        users.put(player.getUniqueId(), new User(player));
    }

    public void removeUser(Player player) {
        users.remove(player.getUniqueId());
    }

}
