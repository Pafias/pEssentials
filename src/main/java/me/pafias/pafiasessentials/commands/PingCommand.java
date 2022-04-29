package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public PingCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (sender.hasPermission("essentials.ping")) {
            if (args.length == 0) {
                Player player = (Player) sender;
                CraftPlayer craftplayer = (CraftPlayer) player;
                EntityPlayer entityplayer = craftplayer.getHandle();
                int ping = entityplayer.ping;
                String sping = (ping < 30 ? ChatColor.DARK_GREEN.toString() + ping : ping < 50 ? ChatColor.GREEN.toString() + ping : ping < 100 ? ChatColor.YELLOW.toString() + ping : ChatColor.RED.toString() + ping);
                player.sendMessage(CC.translate("&6Ping: " + sping + "&7ms"));
            } else {
                if (sender.hasPermission("essentials.ping.others")) {
                    Player target = plugin.getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(CC.translate("&cPlayer not found."));
                        return true;
                    }
                    CraftPlayer craftplayer = (CraftPlayer) target;
                    EntityPlayer entityplayer = craftplayer.getHandle();
                    int ping = entityplayer.ping;
                    String sping = (ping < 30 ? ChatColor.DARK_GREEN.toString() + ping : ping < 50 ? ChatColor.GREEN.toString() + ping : ping < 100 ? ChatColor.YELLOW.toString() + ping : ChatColor.RED.toString() + ping);
                    sender.sendMessage(CC.translate("&d" + target.getName() + (target.getName().endsWith("s") ? "&6'" : "&6's") + " &6ping: " + sping + "&7ms"));
                }
            }
        }
        return true;
    }

}
