package me.pafias.pafiasessentials.commands.freezing;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.events.PlayerFrozenEvent;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class FreezeCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public FreezeCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.freeze")) {
            if (args.length < 1) {
                sender.sendMessage(CC.tf("&c/%s <player>", label));
                return true;
            }
            User target = plugin.getSM().getUserManager().getUser(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return true;
            }
            if (target.isFrozen()) {
                sender.sendMessage(CC.t("&cPlayer already frozen!"));
                return true;
            }
            target.setFrozen(true);
            target.getPlayer().sendMessage("");
            target.getPlayer().sendMessage(CC.tf("&cYou were frozen by &a%s", sender.getName()));
            target.getPlayer().sendMessage("");
            sender.sendMessage(CC.tf("&aYou froze &c%s", target.getRealName()));
            plugin.getServer().getPluginManager().callEvent(new PlayerFrozenEvent(target, sender));
        }
        return true;
    }

}
