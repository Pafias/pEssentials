package me.pafias.pafiasessentials.client.labymod.objects;

import com.google.gson.JsonParser;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.client.labymod.LabyModProtocol;
import me.pafias.pafiasessentials.objects.User;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LabymodUser {

    private User user;

    private String version;
    private boolean ccp;
    private boolean shadow;
    private List<Addon> addons = new ArrayList<>();
    private List<Mod> mods = new ArrayList<>();

    public LabymodUser(Player player, JSONObject json) {
        user = PafiasEssentials.get().getSM().getUserManager().getUser(player);
        version = json.getString("version");
        ccp = json.getJSONObject("ccp").getBoolean("enabled");
        shadow = json.getJSONObject("shadow").getBoolean("enabled");
        if (json.has("addons")) {
            JSONArray a = json.getJSONArray("addons");
            for (int i = 0; i < a.length(); i++) {
                JSONObject addon = a.getJSONObject(i);
                addons.add(new Addon(addon.getString("uuid"), addon.getString("name")));
            }
        }
        if (json.has("mods")) {
            JSONArray m = json.getJSONArray("mods");
            for (int i = 0; i < m.length(); i++) {
                JSONObject mod = m.getJSONObject(i);
                mods.add(new Mod(mod.getString("hash"), mod.getString("name")));
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

    public void sendLabyMessage(Player player, String key, JSONObject json) {
        LabyModProtocol.sendLabyModMessage(player, key, new JsonParser().parse(json.toString()));
    }

}
