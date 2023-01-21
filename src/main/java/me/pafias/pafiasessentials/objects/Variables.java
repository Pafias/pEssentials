package me.pafias.pafiasessentials.objects;

import me.pafias.pafiasessentials.PafiasEssentials;

public class Variables {

    private final PafiasEssentials plugin;

    public Variables(PafiasEssentials plugin) {
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
    }

}
