package me.pafias.pafiasessentials.client.labymod.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.client.labymod.LabyModProtocol;
import me.pafias.pafiasessentials.objects.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LabymodUser {

    private final User user;

    private final String version;
    private final boolean ccp;
    private final boolean shadow;
    private final List<Addon> addons = new ArrayList<>();
    private final List<Mod> mods = new ArrayList<>();

    public LabymodUser(Player player, JsonElement json) {
        user = PafiasEssentials.get().getSM().getUserManager().getUser(player);
        version = json.getAsJsonObject().get("version").getAsString();
        ccp = json.getAsJsonObject().get("ccp").getAsJsonObject().get("enabled").getAsBoolean();
        shadow = json.getAsJsonObject().get("shadow").getAsJsonObject().get("enabled").getAsBoolean();
        if (json.getAsJsonObject().has("addons")) {
            JsonArray a = json.getAsJsonObject().get("addons").getAsJsonArray();
            for (int i = 0; i < a.size(); i++) {
                JsonObject addon = a.get(i).getAsJsonObject();
                addons.add(new Addon(addon.get("uuid").getAsString(), addon.get("name").getAsString()));
            }
        }
        if (json.getAsJsonObject().has("mods")) {
            JsonArray m = json.getAsJsonObject().get("mods").getAsJsonArray();
            for (int i = 0; i < m.size(); i++) {
                JsonObject mod = m.get(i).getAsJsonObject();
                mods.add(new Mod(mod.get("hash").getAsString(), mod.get("name").getAsString()));
            }
        }
    }

    public User getUser() {
        return user;
    }

    public String getVersion() {
        return version;
    }

    public boolean hasCCP() {
        return ccp;
    }

    public boolean hasShadow() {
        return shadow;
    }

    public List<Addon> getAddons() {
        return addons;
    }

    public List<Mod> getMods() {
        return mods;
    }

    public void sendLabyMessage(Player player, String key, JsonElement json) {
        LabyModProtocol.sendLabyModMessage(player, key, json);
    }

}
