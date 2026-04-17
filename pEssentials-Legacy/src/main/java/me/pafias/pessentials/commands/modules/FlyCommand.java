package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FlyCommand extends ICommand {

    public FlyCommand() {
        super(
                "fly",
                "essentials.fly",
                "Toggle flight for yourself or someone else",
                "/fly [player]",
                "flight"
        );
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {

        // /fly (self)
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LCC.t("&cSpecify a player."));
                return;
            }

            final Player player = (Player) sender;

            toggle(player);
            sendMessage(player, player);
            return;
        }

        // /fly <player>
        if (!sender.hasPermission(getPermission() + ".others")) {
            sender.sendMessage(LCC.t("&cYou do not have permission to do this."));
            return;
        }

        final Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
            sender.sendMessage(LCC.t("&cPlayer not found."));
            return;
        }

        toggle(target);
        sendMessage(sender, target);
    }

    // ====================================================
    // Utility
    // ====================================================

    private void toggle(Player player) {
        final boolean enabled = !player.getAllowFlight();
        player.setAllowFlight(enabled);

        if (!enabled) {
            player.setFlying(false);
        }
    }

    private void sendMessage(CommandSender sender, Player target) {
        final boolean enabled = target.getAllowFlight();

        sender.sendMessage(LCC.t(
                enabled
                        ? "&6Flight &dtoggled on &6for &d" + target.getName()
                        : "&6Flight &ctoggled off &6for &d" + target.getName()
        ));
    }

    // ====================================================
    // TAB COMPLETE
    // ====================================================

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        return Collections.emptyList();
    }
}