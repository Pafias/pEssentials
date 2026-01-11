package me.pafias.pessentials.commands.modules;

import com.destroystokyo.paper.ClientOption;
import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import me.pafias.putils.BukkitPlayerManager;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class WhoisCommand extends ICommand {

    public WhoisCommand() {
        super("whois", "essentials.whois", "Info on someone", "/whois <player>", "seen");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            sender.sendMessage(CC.t("&c/whois <player>"));
        else {
            CompletableFuture.supplyAsync(() -> BukkitPlayerManager.getOfflinePlayerByInput(args[0]))
                    .thenAccept(offlinePlayer -> {
                        if (offlinePlayer == null) {
                            sender.sendMessage(CC.t("&cSomething went wrong or that player has never been online before."));
                        } else {
                            User user = null;
                            if (offlinePlayer.isOnline())
                                user = plugin.getSM().getUserManager().getUser(offlinePlayer.getUniqueId());

                            sender.sendMessage("");
                            sender.sendMessage(CC.t("&6UUID: &7" + offlinePlayer.getUniqueId()));
                            if (user != null && user.hasIdentity()) {
                                sender.sendMessage(CC.t("&6Real name: &7" + user.getRealName()));
                                sender.sendMessage(CC.t("&6Disguised name: &7" + user.getName()));
                            } else
                                sender.sendMessage(CC.t("&6Name: &7" + offlinePlayer.getName()));
                            if (user != null)
                                sender.sendMessage(CC.t("&6Ping: &7" + user.getPlayer().getPing()));
                            sender.sendMessage(CC.t("&6First played: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(offlinePlayer.getFirstPlayed()))));
                            sender.sendMessage(CC.t("&6Last login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(offlinePlayer.getLastLogin()))));
                            sender.sendMessage(CC.t("&6Last seen: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(offlinePlayer.getLastSeen()))));
                            try {
                                final Location lastDeathLocation = offlinePlayer.getLastDeathLocation();
                                if (lastDeathLocation != null) {
                                    ClickEvent clickEvent;
                                    try {
                                        clickEvent = ClickEvent.callback(clicker -> {
                                            if (clicker instanceof Player clickerPlayer)
                                                clickerPlayer.teleportAsync(lastDeathLocation);
                                        });
                                    } catch (Throwable t) {
                                        // ClickEvent.callback was added in 1.20
                                        clickEvent = ClickEvent.runCommand("/tp " + lastDeathLocation.getX() + " " + lastDeathLocation.getY() + " " + lastDeathLocation.getZ());
                                    }
                                    sender.sendMessage(
                                            CC.af(
                                                            "&6Last death location: &7%.2f, %.2f, %.2f in world %s",
                                                            lastDeathLocation.getX(), lastDeathLocation.getY(), lastDeathLocation.getZ(),
                                                            lastDeathLocation.getWorld().getName()
                                                    )
                                                    .hoverEvent(HoverEvent.showText(CC.a("&7&oClick to teleport here")))
                                                    .clickEvent(clickEvent)
                                    );
                                }
                            } catch (Throwable t) {
                                // getLastDeathLocation was added in 1.19
                            }
                            final Location bedSpawnLocation = offlinePlayer.getBedSpawnLocation();
                            if (bedSpawnLocation != null) {
                                ClickEvent clickEvent;
                                try {
                                    clickEvent = ClickEvent.callback(clicker -> {
                                        if (clicker instanceof Player clickerPlayer)
                                            clickerPlayer.teleportAsync(bedSpawnLocation);
                                    });
                                } catch (Throwable t) {
                                    // ClickEvent.callback was added in 1.20
                                    clickEvent = ClickEvent.runCommand("/tp " + bedSpawnLocation.getX() + " " + bedSpawnLocation.getY() + " " + bedSpawnLocation.getZ());
                                }
                                sender.sendMessage(
                                        CC.af(
                                                        "&6Bed spawn location: &7%.2f, %.2f, %.2f in world %s",
                                                        bedSpawnLocation.getX(), bedSpawnLocation.getY(), bedSpawnLocation.getZ(),
                                                        bedSpawnLocation.getWorld().getName()
                                                )
                                                .hoverEvent(HoverEvent.showText(CC.a("&7&oClick to teleport here")))
                                                .clickEvent(clickEvent)
                                );
                            }
                            if (user != null) {
                                if (plugin.getServer().getPluginManager().isPluginEnabled("ViaVersion"))
                                    sender.sendMessage(CC.t("&6Minecraft version: &7" +
                                            com.viaversion.viaversion.api.Via.getAPI().getPlayerProtocolVersion(user.getUUID()).getName()));
                                sender.sendMessage(CC.t("&6Client options:"));
                                sender.sendMessage(CC.t("  &b- Brand name: &7" + user.getPlayer().getClientBrandName()));
                                sender.sendMessage(CC.t("  &b- Language: &7" + user.getPlayer().getClientOption(ClientOption.LOCALE)));
                                sender.sendMessage(CC.t("  &b- Chat visibility: &7" + user.getPlayer().getClientOption(ClientOption.CHAT_VISIBILITY)));
                                sender.sendMessage(CC.t("  &b- View distance: &7" + user.getPlayer().getClientOption(ClientOption.VIEW_DISTANCE)));
                                sender.sendMessage(CC.t("  &b- Chat colors enabled: &7" + user.getPlayer().getClientOption(ClientOption.CHAT_COLORS_ENABLED)));
                                sender.sendMessage(CC.t("  &b- Main hand: &7" + user.getPlayer().getClientOption(ClientOption.MAIN_HAND)));
                                sender.sendMessage(CC.t("  &b- Skin parts: &7" + user.getPlayer().getClientOption(ClientOption.SKIN_PARTS).getRaw()));
                                try {
                                    sender.sendMessage(CC.t("  &b- Allowing server listing: &7" + user.getPlayer().getClientOption(ClientOption.ALLOW_SERVER_LISTINGS)));
                                } catch (Throwable ignored) {
                                    // ALLOW_SERVER_LISTINGS was added in 1.19
                                }
                                try {
                                    sender.sendMessage(CC.t("  &b- Text filtering enabled: &7" + user.getPlayer().getClientOption(ClientOption.TEXT_FILTERING_ENABLED)));
                                } catch (Throwable ignored) {
                                    // TEXT_FILTERING_ENABLED was added in 1.19
                                }
                                try {
                                    sender.sendMessage(CC.t("  &b- Particle visibility: &7" + user.getPlayer().getClientOption(ClientOption.PARTICLE_VISIBILITY)));
                                } catch (Throwable ignored) {
                                    // PARTICLE_VISIBILITY was added after 1.21.1, paper api not available for 1.21.2 and 1.21.3, so seen firstly in 1.21.4
                                }
                            }
                            sender.sendMessage("");
                        }
                    });
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> online = plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            if (!online.isEmpty())
                return online;
            else {
                return Arrays.stream(plugin.getServer().getOfflinePlayers())
                        .map(OfflinePlayer::getName)
                        .filter(Objects::nonNull)
                        .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                        .toList();
            }
        } else return Collections.emptyList();
    }

}
