package me.pafias.pessentials.tasks;

import me.pafias.pessentials.pEssentials;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
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
            final CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
            final HttpGet httpGet = new HttpGet();
            if (plugin.parseVersion() < 18)
                httpGet.setURI(new URI("https://pafias.me/minecraft/pEssentials/legacy/pEssentials.jar"));
            else
                httpGet.setURI(new URI("https://pafias.me/minecraft/pEssentials/latest/pEssentials.jar"));
            final HttpResponse httpResponse = closeableHttpClient.execute(httpGet, new BasicHttpContext());

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != 200 || httpResponse.getEntity() == null) {
                plugin.getLogger().warning("Failed to check for updates. HTTP " + statusCode);
                return;
            }

            final File tempJarFile = new File(plugin.getDataFolder(), "pEssentials_update.jar");
            final File jarFileOnPluginFolder = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

            if (jarFileOnPluginFolder.length() != httpResponse.getEntity().getContentLength()) {
                plugin.getLogger().info("Found a new version and downloading the update!");

                try (BufferedInputStream in = new BufferedInputStream(httpResponse.getEntity().getContent());
                     final FileOutputStream out = new FileOutputStream(tempJarFile)) {

                    final byte[] buffer = new byte[1024];
                    int count;
                    while ((count = in.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                    plugin.getLogger().info("Downloaded update successfully.");

                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        try {
                            Files.move(tempJarFile.toPath(), jarFileOnPluginFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            plugin.getLogger().info("Updated to the latest version successfully. Restarting server...");
                            Bukkit.shutdown();
                        } catch (IOException e) {
                            plugin.getLogger().severe("Failed to replace the old JAR file with the new one.");
                            e.printStackTrace();
                        }
                    }, 40);
                }
            }
        } catch (IOException | java.net.URISyntaxException ex) {
            plugin.getLogger().severe("Failed to check for updates.");
            ex.printStackTrace();
        }
    }
}
