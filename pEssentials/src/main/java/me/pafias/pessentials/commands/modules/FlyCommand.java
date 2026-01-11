package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            if (!(sender instanceof Player player)) {
                sender.sendMessage(CC.t("&cSpecify a player."));
                return;
            }

            toggle(player);
            sendMessage(player, player);
            return;
        }

        // /fly <player>
        if (!sender.hasPermission(getPermission() + ".others")) {
            sender.sendMessage(CC.t("&cYou do not have permission to do this."));
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || (sender instanceof Player p && !p.canSee(target))) {
            sender.sendMessage(CC.t("&cPlayer not found."));
            return;
        }

        toggle(target);
        sendMessage(sender, target);
    }

    // ====================================================
    // Utility
    // ====================================================

    private void toggle(Player player) {
        boolean enabled = !player.getAllowFlight();
        player.setAllowFlight(enabled);

        if (!enabled) {
            player.setFlying(false);
        }
    }

    private void sendMessage(CommandSender sender, Player target) {
        boolean enabled = target.getAllowFlight();

        sender.sendMessage(CC.t(
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
        if (args.length != 1) return List.of();

        return plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> !(sender instanceof Player sp) || sp.canSee(p))
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .toList();
    }
}