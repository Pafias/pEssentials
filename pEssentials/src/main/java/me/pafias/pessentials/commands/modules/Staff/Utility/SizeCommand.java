package me.pafias.pessentials.commands.modules.Staff.Utility;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class SizeCommand extends ICommand {

    private static final List<String> VALUES =
            List.of("4", "5", "6", "10", "200", "default");

    public SizeCommand() {
        super(
                "size",
                "essentials.size",
                "Change your size",
                "/size [player] <value|default>"
        );
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(CC.t("&c/" + label + " [player] <value|default>"));
            return;
        }

        // /size <value>
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(CC.t("&cSpecify a player."));
                return;
            }

            if (!apply(player, args[0])) {
                sender.sendMessage(CC.t("&cInvalid Input! Try an existing option."));
                return;
            }

            sendMessage(player, player, args[0]);
            return;
        }

        // /size <player> <value>
        if (args.length == 2) {
            if (!sender.hasPermission(getPermission() + ".others")) {
                sender.sendMessage(CC.t("&cYou do not have permission to change other players' reach!"));
                return;
            }

            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null || (sender instanceof Player sp && !sp.canSee(target))) {
                sender.sendMessage(CC.t("&cPlayer not found!"));
                return;
            }

            if (!apply(target, args[1])) {
                sender.sendMessage(CC.t("&cInvalid reach value!"));
                return;
            }

            sendMessage(sender, target, args[1]);
            return;
        }

        sender.sendMessage(CC.t("&c/" + label + " [player] <value|default>"));
    }

    // ====================================================
    // Utility
    // ====================================================

    private boolean apply(Player player, String input) {
        AttributeInstance attribute =
                player.getAttribute(Attribute.SCALE);
        if (attribute == null) return false;

        if (input.equalsIgnoreCase("default")) {
            attribute.setBaseValue(attribute.getDefaultValue());
            return true;
        }

        try {
            attribute.setBaseValue(Double.parseDouble(input));
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void sendMessage(CommandSender sender, Player target, String input) {
        sender.sendMessage(CC.t(
                input.equalsIgnoreCase("default")
                        ? "&6Size reset to &ddefault &6for &d" + target.getName() + "\n\n&c&oPlease note that this only applies to clients on versions &e1.20.6 and above."
                        : "&6Size set to &d" + input + " &6for &d" + target.getName() + "\n\n&c&oPlease note that this only applies to clients on versions &e1.20.6 and above."

                // Due to older versions, the attribute may only be applied to clients on versions 1.21.6+!
        ));
    }

    // ====================================================
    // TAB COMPLETE
    // ====================================================

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {

        // /size <value>
        if (args.length == 1) {
            Stream<String> values = VALUES.stream();
            Stream<String> players = sender.hasPermission(getPermission() + ".others")
                    ? plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> !(sender instanceof Player sp) || sp.canSee(p))
                    .map(Player::getName)
                    : Stream.empty();

            return Stream.concat(values, players)
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }

        // /size <player> <value>
        if (args.length == 2 && sender.hasPermission(getPermission() + ".others")) {
            return VALUES.stream()
                    .filter(v -> v.startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
