package me.pafias.pafiasessentials.services;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserManager {

    private PafiasEssentials plugin;

    private Set<User> users = new HashSet<>();

    public UserManager(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    public Set<User> getUsers() {
        return users;
    }

    public User getUser(UUID uuid) {
        return users.stream().filter(u -> u.getUUID().equals(uuid)).findAny().orElse(null);
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public User getUser(String name) {
        return users.stream().filter(u -> u.getName().toLowerCase().startsWith(name.toLowerCase().trim())).findAny().orElse(null);
    }

    public void addUser(Player player) {
        users.add(new User(player));
    }

    public void removeUser(Player player) {
        User user = getUser(player);
        users.remove(user);
    }

}
