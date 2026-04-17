package me.pafias.pessentials.commands.modules;

import com.destroystokyo.paper.ClientOption;
import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.pessentials.util.Reflection;
import me.pafias.putils.LCC;
import me.pafias.putils.Tasks;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WhoisCommand extends ICommand {

    public WhoisCommand() {
        super("whois", "essentials.whois", "Info on someone", "/whois <player>", "seen");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            sender.sendMessage(LCC.t("&c/whois <player>"));
        else {
            if (plugin.getServer().getPlayer(args[0]) == null) {
                Tasks.runAsync(() -> {
                    final OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[0]);
                    if (!player.hasPlayedBefore()) {
                        sender.sendMessage(LCC.t("&cThat player has never been online before."));
                    } else {
                        sender.sendMessage("");
                        sender.sendMessage(LCC.t("&6UUID: &7" + player.getUniqueId()));
                        sender.sendMessage(LCC.t("&6Name: &7" + player.getName()));
                        sender.sendMessage(LCC.t("&6First login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(player.getFirstPlayed()))));
                        sender.sendMessage(LCC.t("&6Last seen: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(player.getLastSeen()))));
                        sender.sendMessage("");
                    }
                });
            } else {
                final User user = plugin.getSM().getUserManager().getUser(args[0]);
                sender.sendMessage("");
                sender.sendMessage(LCC.t("&6UUID: &7" + user.getUUID().toString()));
                if (user.hasIdentity()) {
                    sender.sendMessage(LCC.t("&6Real name: &7" + user.getRealName()));
                    sender.sendMessage(LCC.t("&6Disguised name: &7" + user.getName()));
                } else {
                    sender.sendMessage(LCC.t("&6Name: &7" + user.getName()));
                }
                sender.sendMessage(LCC.t("&6First login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(user.getPlayer().getFirstPlayed()))));
                sender.sendMessage(LCC.t("&6Ping: &7" + Reflection.getPing(user.getPlayer())));
                if (plugin.parseVersion() >= 16) {
                    sender.sendMessage(LCC.t("&6Client options:"));
                    sender.sendMessage(LCC.t("  &b- Language: &7" + user.getPlayer().getClientOption(ClientOption.LOCALE)));
                    sender.sendMessage(LCC.t("  &b- Chat visibility: &7" + user.getPlayer().getClientOption(ClientOption.CHAT_VISIBILITY).name().toLowerCase()));
                    sender.sendMessage(LCC.t("  &b- View distance: &7" + user.getPlayer().getClientOption(ClientOption.VIEW_DISTANCE)));
                }
                sender.sendMessage("");
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        else return Collections.emptyList();
    }

}
