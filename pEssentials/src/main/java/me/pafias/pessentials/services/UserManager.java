package me.pafias.pessentials.services;

import lombok.Getter;
import me.pafias.pessentials.objects.ConsoleUser;
import me.pafias.pessentials.objects.User;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class UserManager {

    @Getter
    private final ConsoleUser consoleUser = new ConsoleUser();

    @Getter
    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    private final Map<String, UUID> namesLower = new ConcurrentHashMap<>();

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public User getUser(UUID uuid, Predicate<User> predicate) {
        final User user = users.get(uuid);
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
        final User exact = getUserByExactName(name);
        if (exact != null)
            return exact;
        final String nameLower = name.toLowerCase(Locale.ROOT).trim();
        for (User user : users.values()) {
            if (user.getName().toLowerCase(Locale.ROOT).startsWith(nameLower))
                return user;
        }
        return null;
    }

    public User getUser(String name, Predicate<User> predicate) {
        final User exact = getUserByExactName(name);
        if (exact != null && predicate.test(exact))
            return exact;
        final String nameLower = name.toLowerCase(Locale.ROOT).trim();
        for (User user : users.values()) {
            if (user.getName().toLowerCase(Locale.ROOT).startsWith(nameLower) && predicate.test(user))
                return user;
        }
        return null;
    }

    private User getUserByExactName(String name) {
        final UUID uuid = namesLower.get(name.toLowerCase(Locale.ROOT));
        return uuid == null ? null : users.get(uuid);
    }

    public void addUser(Player player) {
        final User user = new User(player);
        users.put(player.getUniqueId(), user);
        namesLower.put(user.getName().toLowerCase(Locale.ROOT), player.getUniqueId());
    }

    public void removeUser(Player player) {
        final User user = users.remove(player.getUniqueId());
        if (user != null)
            namesLower.remove(user.getName().toLowerCase(Locale.ROOT), player.getUniqueId());
    }

    public void updateNameIndex(UUID uuid, String oldName, String newName) {
        if (oldName != null)
            namesLower.remove(oldName.toLowerCase(Locale.ROOT), uuid);
        if (newName != null)
            namesLower.put(newName.toLowerCase(Locale.ROOT), uuid);
    }

    public void shutdown() {
        for (User user : users.values()) {
            user.setIdentity(user.getOriginalGameProfile());
        }
    }
}
