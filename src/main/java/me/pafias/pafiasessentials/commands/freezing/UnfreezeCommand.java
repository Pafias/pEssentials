package me.pafias.pafiasessentials.commands.freezing;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.objects.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnfreezeCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public UnfreezeCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("essentials.unfreeze")) {
            if (args.length < 1) {
                sender.sendMessage(CC.tf("&c/%s <player>", label));
                return true;
            }
            User target = plugin.getSM().getUserManager().getUser(args[0]);
            if (target == null) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return true;
            }
            if (!target.isFrozen()) {
                sender.sendMessage(CC.t("&cPlayer not frozen!"));
                return true;
            }
            target.setFrozen(false);
            target.getPlayer().sendMessage("");
            target.getPlayer().sendMessage(CC.t("&aYou are now unfrozen"));
            target.getPlayer().sendMessage("");
            sender.sendMessage(CC.tf("&aYou unfroze &c%s", target.getRealName()));
        }
        return true;
    }

}
