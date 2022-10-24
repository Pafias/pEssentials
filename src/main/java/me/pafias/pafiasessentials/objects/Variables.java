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
    public String staffchatFormat = "[Staff] {player}: {message}";
    public boolean antilog4j = true;
    public String serverName = "Minecraft Server";
    public boolean hidePlugins = true;

    public boolean joinMessage = true;
    public boolean quitMessage = true;
    public int pingKickThreshold = 150;
    public String rickrollResourcePack;

    private void reloadConfigYML() {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
        hubServer = plugin.getConfig().getString("hub_server");
        staffchatFormat = plugin.getConfig().getString("staffchat_format");
        antilog4j = plugin.getConfig().getBoolean("log4j_exploit_protection");
        serverName = plugin.getConfig().getString("server_name");
        hidePlugins = plugin.getConfig().getBoolean("hide_plugins");
        joinMessage = plugin.getConfig().getBoolean("join_message_enabled");
        quitMessage = plugin.getConfig().getBoolean("quit_message_enabled");
        pingKickThreshold = plugin.getConfig().getInt("ping_kick_threshold");
        rickrollResourcePack = plugin.getConfig().getString("rickroll_resourcepack_link");
    }

}
