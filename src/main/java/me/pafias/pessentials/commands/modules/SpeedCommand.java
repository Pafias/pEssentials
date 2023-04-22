package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpeedCommand extends ICommand {

    public SpeedCommand() {
        super("speed", "essentials.speed", "Change walk/fly speed", "/speed <multiplier>/reset [player]");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.speed")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players!"));
                    return;
                }
                Player player = (Player) sender;
                sender.sendMessage(CC.t("&6Currently, your walking speed is &7" + player.getWalkSpeed() + " &6and your flying speed is &7" + player.getFlySpeed()));
                sender.sendMessage(CC.t("&6Use /speed <speed> to change it"));
            } else if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.t("&cOnly players!"));
                    return;
                }
                Player player = (Player) sender;
                boolean flying = player.isFlying();
                float speed;
                if (args[0].equalsIgnoreCase("reset"))
                    speed = 1;
                else
                    speed = getMoveSpeed(args[0]);
                if (flying) {
                    player.setFlySpeed(getRealMoveSpeed(speed, flying));
                    player.sendMessage(CC.t("&6Your fly speed multiplier is now &7" + speed));
                } else {
                    player.setWalkSpeed(getRealMoveSpeed(speed, flying));
                    player.sendMessage(CC.t("&6Your walk speed multiplier is now &7" + speed));
                }
            } else {
                if (sender.hasPermission("essentials.speed.others")) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(CC.t("&cPlayer not found!"));
                        return;
                    }
                    boolean flying = target.isFlying();
                    float speed;
                    if (args[0].equalsIgnoreCase("reset"))
                        speed = 1;
                    else
                        speed = getMoveSpeed(args[0]);
                    if (flying) {
                        target.setFlySpeed(getRealMoveSpeed(speed, flying));
                        target.sendMessage(CC.t("&6Your fly speed multiplier is now &7" + speed));
                        sender.sendMessage(CC.t("&6Changed players' fly speed multiplier to &7" + speed));
                    } else {
                        target.setWalkSpeed(getRealMoveSpeed(speed, flying));
                        target.sendMessage(CC.t("&6Your walk speed multiplier is now &7" + speed));
                        sender.sendMessage(CC.t("&6Changed players' walk speed multiplier to &7" + speed));
                    }
                }
            }
            return;
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2)
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
        else return Collections.emptyList();
    }

    private float getMoveSpeed(final String moveSpeed) {
        float userSpeed = 1;
        try {
            userSpeed = Float.parseFloat(moveSpeed);
            if (userSpeed > 10f) {
                userSpeed = 10f;
            } else if (userSpeed < 0.0001f) {
                userSpeed = 0.0001f;
            }
        } catch (NumberFormatException e) {

        }
        return userSpeed;
    }

    private float getRealMoveSpeed(final float userSpeed, final boolean isFly) {
        final float defaultSpeed = isFly ? 0.1f : 0.2f;
        float maxSpeed = 1f;
        if (userSpeed < 1f) {
            return defaultSpeed * userSpeed;
        } else {
            float ratio = ((userSpeed - 1) / 9) * (maxSpeed - defaultSpeed);
            return ratio + defaultSpeed;
        }
    }

}
