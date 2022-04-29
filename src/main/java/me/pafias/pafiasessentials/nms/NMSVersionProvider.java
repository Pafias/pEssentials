package me.pafias.pafiasessentials.nms;

import org.bukkit.Bukkit;

public class NMSVersionProvider {

    public static NMSProvider getProvider() {
        String[] split = Bukkit.getBukkitVersion().split("\\.");
        String serverVersion = split[0] + "_" + split[1] + "_R" + split[3].split("\\-")[0];
        try {
            Class<? extends NMSProvider> providerClass = Class.forName("me.pafias.pafiasessentials.nms.VersionProvider" + serverVersion).asSubclass(NMSProvider.class);
            return providerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not find version provider ", e);
        }
    }

}
