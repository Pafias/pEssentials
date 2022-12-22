package me.pafias.pafiasessentials.commands.modules;

import com.destroystokyo.paper.ClientOption;
import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import me.pafias.pafiasessentials.util.Reflection;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WhoisCommand extends ICommand {

    public WhoisCommand() {
        super("whois", "essentials.whois", "Info on someone", "/whois <player>", "seen");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.whois")) {
            if (args.length == 0) {
                sender.sendMessage(CC.t("&c/whois <player>"));
                return;
            } else {
                if (plugin.getServer().getPlayer(args[0]) == null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[0]);
                            if (!player.hasPlayedBefore()) {
                                sender.sendMessage(CC.t("&cThat player has never been online before."));
                                return;
                            } else {
                                sender.sendMessage("");
                                sender.sendMessage(CC.t("&6UUID: &7" + player.getUniqueId()));
                                sender.sendMessage(CC.t("&6Name: &7" + player.getName()));
                                sender.sendMessage(CC.t("&6First login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(player.getFirstPlayed()))));
                                sender.sendMessage(CC.t("&6Last seen: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(player.getLastSeen()))));
                                sender.sendMessage("");
                            }
                        }
                    }.runTaskAsynchronously(plugin);
                } else {
                    User user = plugin.getSM().getUserManager().getUser(args[0]);
                    sender.sendMessage("");
                    sender.sendMessage(CC.t("&6UUID: &7" + user.getUUID().toString()));
                    if (user.hasIdentity()) {
                        sender.sendMessage(CC.t("&6Real name: &7" + user.getRealName()));
                        sender.sendMessage(CC.t("&6Disguised name: &7" + user.getName()));
                    } else {
                        sender.sendMessage(CC.t("&6Name: &7" + user.getName()));
                    }
                    sender.sendMessage(CC.t("&6First login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(user.getPlayer().getFirstPlayed()))));
                    sender.sendMessage(CC.t("&6Ping: &7" + Reflection.getPing(user.getPlayer())));
                    if (plugin.parseVersion() >= 16) {
                        sender.sendMessage(CC.t("&6Client options:"));
                        sender.sendMessage(CC.t("  &b- Language: &7" + user.getPlayer().getClientOption(ClientOption.LOCALE)));
                        sender.sendMessage(CC.t("  &b- Chat visibility: &7" + user.getPlayer().getClientOption(ClientOption.CHAT_VISIBILITY).name().toLowerCase()));
                        sender.sendMessage(CC.t("  &b- View distance: &7" + user.getPlayer().getClientOption(ClientOption.VIEW_DISTANCE)));
                    }
                    sender.sendMessage("");
                }
            }
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
