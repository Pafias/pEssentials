package me.pafias.pafiasessentials.commands;

import com.destroystokyo.paper.ClientOption;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.client.labymod.objects.LabymodUser;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import me.pafias.pafiasessentials.util.RandomUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WhoisCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public WhoisCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.whois")) {
            if (args.length == 0) {
                sender.sendMessage(CC.translate("&c/whois <player>"));
                return true;
            } else {
                if (plugin.getServer().getPlayer(args[0]) == null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[0]);
                            if (!player.hasPlayedBefore()) {
                                sender.sendMessage(CC.translate("That player has never been online before."));
                                return;
                            } else {
                                sender.sendMessage("");
                                sender.sendMessage(CC.translate("&6UUID: &7" + player.getUniqueId()));
                                sender.sendMessage(CC.translate("&6Name: &7" + player.getName()));
                                sender.sendMessage(CC.translate("&6First login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(player.getFirstPlayed()))));
                                sender.sendMessage(CC.translate("&6Last seen: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(player.getLastSeen()))));
                                sender.sendMessage("");
                            }
                        }
                    }.runTaskAsynchronously(plugin);
                } else {
                    User user = plugin.getSM().getUserManager().getUser(args[0]);
                    sender.sendMessage("");
                    sender.sendMessage(CC.translate("&6UUID: &7" + user.getUUID().toString()));
                    if (user.hasIdentity()) {
                        sender.sendMessage(CC.translate("&6Real name: &7" + user.getRealName()));
                        sender.sendMessage(CC.translate("&6Disguised name: &7" + user.getName()));
                    } else {
                        sender.sendMessage(CC.translate("&6Name: &7" + user.getName()));
                    }
                    sender.sendMessage(CC.translate("&6First login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(user.getPlayer().getFirstPlayed()))));
                    sender.sendMessage(CC.translate("&6Ping: &7" + plugin.getSM().getNMSProvider().getPing(user.getPlayer())));
                    if (RandomUtils.isRecentVersion()) {
                        sender.sendMessage(CC.translate("&6Client options:"));
                        sender.sendMessage(CC.translate("  &b- Language: &7" + user.getPlayer().getClientOption(ClientOption.LOCALE)));
                        sender.sendMessage(CC.translate("  &b- Chat visibility: &7" + user.getPlayer().getClientOption(ClientOption.CHAT_VISIBILITY).name().toLowerCase()));
                        sender.sendMessage(CC.translate("  &b- View distance: &7" + user.getPlayer().getClientOption(ClientOption.VIEW_DISTANCE)));
                    }
                    if (plugin.getSM().getLabymodManager().usingLabymod(user)) {
                        sender.sendMessage(CC.translate("&6LabyMod: &ayes"));
                        LabymodUser lu = plugin.getSM().getLabymodManager().getLabymodUser(user);
                        if (lu != null) {
                            if (!lu.getAddons().isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                lu.getAddons().forEach(addon -> {
                                    sb.append(addon.getName()).append(lu.getAddons().indexOf(addon) == lu.getAddons().size() - 1 ? "" : CC.translate("&c, &7"));
                                });
                                sender.sendMessage(CC.translate("  &b- Addons: &7" + sb.toString()));
                            }
                            if (!lu.getMods().isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                lu.getMods().forEach(mod -> {
                                    sb.append(mod.getName()).append(lu.getMods().indexOf(mod) == lu.getMods().size() - 1 ? "" : CC.translate("&c, &7"));
                                });
                                sender.sendMessage(CC.translate("  &b- Mods: &7" + sb.toString()));
                            }
                        }
                    }
                    sender.sendMessage("");
                }
            }
        }
        return true;
    }

}
