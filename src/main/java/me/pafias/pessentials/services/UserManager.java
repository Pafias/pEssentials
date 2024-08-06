package me.pafias.pessentials.services;

import me.pafias.pessentials.objects.User;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final Map<UUID, User> users = new HashMap<>();

    public Map<UUID, User> getUsers() {
        return users;
    }

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public User getUser(String name) {
        return users.values().stream().filter(u -> u.getName().equalsIgnoreCase(name) || u.getName().toLowerCase().startsWith(name.toLowerCase().trim())).findFirst().orElse(null);
    }

    public void addUser(Player player) {
        users.put(player.getUniqueId(), new User(player));
    }

    public void removeUser(Player player) {
        users.remove(player.getUniqueId());
    }

}
