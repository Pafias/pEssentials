package me.pafias.pessentials.tasks;

import me.pafias.pessentials.pEssentials;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AutoUpdaterTask extends BukkitRunnable {

    private final pEssentials plugin;

    public AutoUpdaterTask(pEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            URL url = new java.net.URL("https://pafias.me/minecraft/pEssentials/verylegacy/pEssentials.jar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                final File tempJarFile = new File(plugin.getDataFolder(), "pEssentials_update.jar");
                final File jarFileOnPluginFolder = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

                int contentLength = connection.getContentLength();
                if (jarFileOnPluginFolder.length() != contentLength) {
                    plugin.getLogger().info("Found a new version and downloading the update!");

                    try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                         FileOutputStream out = new FileOutputStream(tempJarFile)) {

                        final byte[] buffer = new byte[1024];
                        int count;
                        while ((count = in.read(buffer)) != -1) {
                            out.write(buffer, 0, count);
                        }
                        plugin.getLogger().info("Downloaded update successfully.");

                        plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    Files.move(tempJarFile.toPath(), jarFileOnPluginFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    plugin.getLogger().info("Updated to the latest version successfully. Restarting server...");
                                    Bukkit.shutdown();
                                } catch (IOException e) {
                                    plugin.getLogger().severe("Failed to replace the old JAR file with the new one.");
                                    e.printStackTrace();
                                }
                            }
                        }, 40);
                    }
                }
            } else {
                plugin.getLogger().severe("Failed to check for updates. HTTP response code: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException | URISyntaxException ex) {
            plugin.getLogger().severe("Failed to check for updates.");
            ex.printStackTrace();
        }
    }
}
