package me.pafias.pessentials.commands.modules.Staff.Moderation;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class UnfreezeCommand extends ICommand {

    public UnfreezeCommand() {
        super("unfreeze", "essentials.unfreeze", "Unfreeze someone", "/unfreeze <player>");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.tf("&c/%s <player>", label));
            return;
        }
        final User target = plugin.getSM().getUserManager().getUser(args[0]);
        if (target == null || (sender instanceof Player senderPlayer && !senderPlayer.canSee(target.getPlayer()))) {
            sender.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        if (!target.isFrozen()) {
            sender.sendMessage(CC.t("&cPlayer not frozen!"));
            return;
        }
        target.setFrozen(false);
        target.getPlayer().sendMessage("");
        target.getPlayer().sendMessage(CC.t("&aYou are now unfrozen"));
        target.getPlayer().sendMessage("");
        sender.sendMessage(CC.tf("&aYou unfroze &c%s", target.getRealName()));
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        else return Collections.emptyList();
    }

}
