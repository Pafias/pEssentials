package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.Reflection;
import me.pafias.pessentials.util.Tasks;
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

public class WhoisCommand extends ICommand {

    public WhoisCommand() {
        super("whois", "essentials.whois", "Info on someone", "/whois <player>", "seen");
    }

    @Override
    public void commandHandler(@NotNull final CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull final String[] args) {
        if (args.length == 0)
            sender.sendMessage(CC.t("&c/whois <player>"));
        else {
            if (plugin.getServer().getPlayer(args[0]) == null) {
                Tasks.runAsync(new BukkitRunnable() {
                    @Override
                    public void run() {
                        final OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[0]);
                        if (!player.hasPlayedBefore()) {
                            sender.sendMessage(CC.t("&cThat player has never been online before."));
                        } else {
                            sender.sendMessage("");
                            sender.sendMessage(CC.t("&6Name: &7" + player.getName()));
                            sender.sendMessage(CC.t("&6First login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(player.getFirstPlayed()))));
                            sender.sendMessage(CC.t("&6Last seen: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(player.getLastPlayed()))));
                            sender.sendMessage("");
                        }
                    }
                });
            } else {
                final User user = plugin.getSM().getUserManager().getUser(args[0]);
                sender.sendMessage("");
                sender.sendMessage(CC.t("&6UUID: &7" + user.getUUID().toString()));
                sender.sendMessage(CC.t("&6Name: &7" + user.getName()));
                sender.sendMessage(CC.t("&6First login: &7" + new SimpleDateFormat("dd MM yyyy @ HH:mm:ss").format(new Date(user.getPlayer().getFirstPlayed()))));
                sender.sendMessage(CC.t("&6Ping: &7" + Reflection.getPing(user.getPlayer())));
                sender.sendMessage("");
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> names = new java.util.ArrayList<>();
            String prefix = args[0].toLowerCase();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String n = p.getName();
                    if (n != null && n.toLowerCase().startsWith(prefix))
                        names.add(n);
                }
            }
            return names;
        } else return Collections.emptyList();
    }

}
