package me.pafias.pessentials.commands.modules;

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
        if (target == null) {
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
        sender.sendMessage(CC.tf("&aYou unfroze &c%s", target.getName()));
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
