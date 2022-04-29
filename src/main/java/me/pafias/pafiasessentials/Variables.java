package me.pafias.pafiasessentials;

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

    private void reloadConfigYML() {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
        hubServer = plugin.getConfig().getString("hub_server");
        staffchatFormat = plugin.getConfig().getString("staffchat_format");
        antilog4j = plugin.getConfig().getBoolean("log4j_exploit_protection");
    }

}
