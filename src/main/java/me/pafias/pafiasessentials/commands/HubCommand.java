package me.pafias.pafiasessentials.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public HubCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cOnly players!"));
            return true;
        }
        String hubserver = plugin.getSM().getVariables().hubServer;
        if (hubserver == null) {
            sender.sendMessage(CC.translate("&cInvalid hub server. Contact server admin."));
            return true;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(hubserver);
        Player player = ((Player) sender);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        return true;
    }

}
