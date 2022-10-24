package me.pafias.pafiasessentials.commands;

import com.mojang.authlib.GameProfile;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import me.pafias.pafiasessentials.util.GameProfileBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RickrollCommand implements CommandExecutor, Listener {

    private final PafiasEssentials plugin;

    public RickrollCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    GameProfile profile = new GameProfile(UUID.randomUUID(), "RickAstley");
                    GameProfile skinGP = GameProfileBuilder.fetch(UUID.fromString("782028d8-6e0b-414b-b714-eb1d8fae1f93"));
                    profile.getProperties().clear();
                    skinGP.getProperties().keySet().forEach(s -> skinGP.getProperties().get(s).forEach(p -> profile.getProperties().put(s, p)));
                    rickastley = profile;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public static GameProfile rickastley;

    public static Map<UUID, Object> entities = new HashMap<>();
    public static Map<UUID, CommandSender> requested = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.rickroll")) {
            if (args.length < 1) {
                sender.sendMessage(CC.tf("&c/%s <player/all>", label));
                return true;
            }
            String link = plugin.getSM().getVariables().rickrollResourcePack;
            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                plugin.getServer().getOnlinePlayers().forEach(p -> {
                    if (!requested.containsKey(p.getUniqueId())) {
                        requested.put(p.getUniqueId(), sender);
                        if (p.hasResourcePack()) plugin.getSM().getNMSProvider().rickroll(p, sender);
                        else p.setResourcePack(link);
                    }
                });
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found"));
                return true;
            }
            if (requested.containsKey(target.getUniqueId())) {
                sender.sendMessage(CC.t("&cThat player already has a rickroll request!"));
                return true;
            }
            requested.put(target.getUniqueId(), sender);
            if (target.hasResourcePack()) plugin.getSM().getNMSProvider().rickroll(target, sender);
            else target.setResourcePack(link);
        }
        return true;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getSM().getNMSProvider().handleRickrollQuit(event);
    }

    @EventHandler
    public void onResourcePack(PlayerResourcePackStatusEvent event) {
        if (requested.containsKey(event.getPlayer().getUniqueId()) && event.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)) {
            requested.get(event.getPlayer().getUniqueId()).sendMessage(CC.tf("&7%s &cdeclined the resourcepack request, rickroll failed :(", event.getPlayer().getName()));
            requested.remove(event.getPlayer().getUniqueId());
            return;
        } else if (requested.containsKey(event.getPlayer().getUniqueId()) && event.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            requested.get(event.getPlayer().getUniqueId()).sendMessage(CC.tf("&7%s &cfailed to download the resourcepack.", event.getPlayer().getName()));
            requested.remove(event.getPlayer().getUniqueId());
            return;
        } else if (requested.containsKey(event.getPlayer().getUniqueId()) && event.getStatus().equals(PlayerResourcePackStatusEvent.Status.ACCEPTED)) {
            requested.get(event.getPlayer().getUniqueId()).sendMessage(CC.tf("&7%s &aaccepted the resourcepack request and should get rickrolled soon.", event.getPlayer().getName()));
            requested.remove(event.getPlayer().getUniqueId());
            return;
        } else if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getSM().getNMSProvider().rickroll(event.getPlayer(), requested.get(event.getPlayer().getUniqueId()));
                    requested.get(event.getPlayer().getUniqueId()).sendMessage(CC.tf("&aYou rickrolled &7%s", event.getPlayer().getName()));
                }
            }.runTaskLater(plugin, 20);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        plugin.getSM().getNMSProvider().handleRickrollMove(event);
    }

}
