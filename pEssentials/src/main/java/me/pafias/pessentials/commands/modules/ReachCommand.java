package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class ReachCommand extends ICommand implements Listener {

    public ReachCommand() {
        super(
                "reach",
                "essentials.reach",
                "Change your reach",
                "/reach <input> [player]"
        );
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (reached.contains(event.getPlayer().getUniqueId())) {
            final AttributeInstance attributeInstance = event.getPlayer().getAttribute(Attribute.ENTITY_INTERACTION_RANGE);
            if (attributeInstance != null)
                attributeInstance.setBaseValue(attributeInstance.getDefaultValue());
            reached.remove(event.getPlayer().getUniqueId());
        }
    }

    private final Set<UUID> reached = new HashSet<>();

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(CC.t("&cSpecify a player."));
                return;
            }

            if (apply(sender, player, args[0]))
                sendMessage(sender, player, args[0]);
            return;
        } else if (args.length == 2) {
            if (!sender.hasPermission(getPermission() + ".others")) {
                sender.sendMessage(CC.t("&cYou do not have permission to modify others."));
                return;
            }

            final Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null || (sender instanceof Player p && !p.canSee(target))) {
                sender.sendMessage(CC.t("&cPlayer not found."));
                return;
            }

            if (apply(sender, target, args[0]))
                sendMessage(sender, target, args[0]);
            return;
        }
        sender.sendMessage(CC.tf("&c/%s <value> [player]", label));
    }

    private void sendMessage(CommandSender sender, Player target, String value) {
        String action = value.equalsIgnoreCase("reset") ? "&dreset" : "set to &d" + value;
        sender.sendMessage(CC.t("&6Reach " + action + " &6for &d" + target.getName()));
    }

    private boolean apply(CommandSender sender, Player target, String input) {
        final Attribute attribute;
        try {
            attribute = Attribute.ENTITY_INTERACTION_RANGE;
        } catch (NoSuchFieldError e) {
            sender.sendMessage(CC.t("&cThe range attribute is only available on &e1.20.6&c or above."));
            return false;
        }
        final AttributeInstance attributeInstance = target.getAttribute(attribute);
        if (attributeInstance == null) {
            sender.sendMessage(CC.t("&cSomething went wrong while trying to get the reach attribute."));
            return false;
        }

        if (input.equalsIgnoreCase("reset") || input.equalsIgnoreCase("default")) {
            attributeInstance.setBaseValue(attributeInstance.getDefaultValue());
            reached.remove(target.getUniqueId());
            return true;
        }

        try {
            double val = Double.parseDouble(input);
            attributeInstance.setBaseValue(val);
            reached.add(target.getUniqueId());
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.t("&cInvalid input: &e" + input));
            return false;
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].isBlank()) {
                return Arrays.asList("reset", "<value>");
            } else {
                String value;
                try {
                    value = String.valueOf(Double.parseDouble(args[0]));
                } catch (NumberFormatException ex) {
                    if ("reset".startsWith(args[0].toLowerCase()))
                        value = "reset";
                    else if ("default".startsWith(args[0].toLowerCase()))
                        value = "default";
                    else
                        value = "Invalid value!";
                }
                return Collections.singletonList(value);
            }
        } else if (args.length == 2)
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        return Collections.emptyList();
    }
}