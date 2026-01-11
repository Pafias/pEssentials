package me.pafias.pessentials.commands.modules.staff.fun;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.ViaUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Stream;

public class SizeCommand extends ICommand {

    private final List<String> VALUES = List.of("5", "10", "reset");

    public SizeCommand() {
        super(
                "size",
                "essentials.size",
                "Change your size",
                "/size <Input> [player]"
        );
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {

        // /size <self>
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(CC.t("&cSpecify a player."));
                return;
            }

            if (apply(sender, player, args[0])) {
                sendMessage(sender, player, args[0]);
            }
            return;
        }

        // /size <player>
        if (args.length == 2) {
            if (!sender.hasPermission(getPermission() + ".others")) {
                sender.sendMessage(CC.t("&cYou do not have permission to modify others."));
                return;
            }

            Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null || (sender instanceof Player p && !p.canSee(target))) {
                sender.sendMessage(CC.t("&cPlayer not found."));
                return;
            }

            if (apply(sender, target, args[0])) {
                sendMessage(sender, target, args[0]);
            }
            return;
        }

        sender.sendMessage(CC.tf("&c/%s <value> [player]", label));
    }

    // ====================================================
    // Utility
    // ====================================================

    private void sendMessage(CommandSender sender, Player target, String value) {
        String action = value.equalsIgnoreCase("reset") ? "reset" : "set to" + value;
        sender.sendMessage(CC.t("&6Size &d" + action + " &6for &d" + target.getName()));
    }

    private boolean apply(CommandSender sender, Player target, String input) {
        if (!ViaUtils.isAtLeast1_20_6(target)) {
            String name = (sender instanceof Player p && p.equals(target)) ? "You are" : target.getName() + " is";
            sender.sendMessage(CC.t("&c" + name + " on an old version!\nThis command is only supported for clients on &e1.20.6&c and above."));
            return false;
        }

        AttributeInstance attribute = target.getAttribute(Attribute.SCALE);
        if (attribute == null) {
            sender.sendMessage(CC.t("&cThis command isn't supported for the current server version.\nUpdate to &e1.20.6&c or above to use it."));
            return false;
        }

        if (input.equalsIgnoreCase("reset") || input.equalsIgnoreCase("default")) {
            attribute.setBaseValue(attribute.getDefaultValue());
            return true;
        }

        try {
            double val = Double.parseDouble(input);
            attribute.setBaseValue(val);
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.t("&cInvalid input: &e" + input));
            return false;
        }
    }


    // ====================================================
    // TAB COMPLETE
    // ====================================================

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return VALUES.stream()
                    .filter(v -> v.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            return plugin.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return List.of();
    }
}