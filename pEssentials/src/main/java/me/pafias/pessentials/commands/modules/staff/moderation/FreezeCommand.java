package me.pafias.pessentials.commands.modules.staff.moderation;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.events.PlayerFrozenEvent;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class FreezeCommand extends ICommand {

    public FreezeCommand() {
        super("freeze", "essentials.freeze", "Freeze someone", "/freeze <player>");
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
        if (target.isFrozen()) {
            sender.sendMessage(CC.t("&cPlayer already frozen!"));
            return;
        }
        target.setFrozen(true);
        target.getPlayer().sendMessage("");
        target.getPlayer().sendMessage(CC.tf("&cYou were frozen by &a%s", sender.getName()));
        target.getPlayer().sendMessage("");
        sender.sendMessage(CC.tf("&aYou froze &c%s", target.getRealName()));
        plugin.getServer().getPluginManager().callEvent(new PlayerFrozenEvent(target, sender));
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
