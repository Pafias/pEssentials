package me.pafias.pessentials.objects;

import me.pafias.pessentials.pEssentials;

import java.util.HashSet;
import java.util.Set;

public class Variables {

    private final pEssentials plugin;

    public Variables(pEssentials plugin) {
        this.plugin = plugin;
        reloadConfigs();
    }

    public void reloadConfigs() {
        reloadConfigYML();
    }

    // config.yml
    public String hubServer = "hub";
    public String staffchatFormat = "&e&l[STAFF] &r&9{player}&r: {message}";
    public boolean hidePlugins = true;

    public boolean joinMessage = true;
    public boolean quitMessage = true;
    public boolean highPingKick = false;
    public int pingKickThreshold = 150;
    public String discordLink;
    public Set<String> disabledCommands = new HashSet<>();

    private void reloadConfigYML() {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
        hubServer = plugin.getConfig().getString("hub_server");
        staffchatFormat = plugin.getConfig().getString("staffchat_format");
        hidePlugins = plugin.getConfig().getBoolean("hide_plugins");
        joinMessage = plugin.getConfig().getBoolean("join_message_enabled");
        quitMessage = plugin.getConfig().getBoolean("quit_message_enabled");
        highPingKick = plugin.getConfig().getBoolean("kick_on_high_ping");
        pingKickThreshold = plugin.getConfig().getInt("ping_kick_threshold");
        discordLink = plugin.getConfig().getString("discord_link");
        disabledCommands = new HashSet<>(plugin.getConfig().getStringList("disabled_commands"));
    }

}
