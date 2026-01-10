package me.pafias.pessentials.commands.modules.Player.Server;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class HubCommand extends ICommand {

    public HubCommand() {
        super("hub", "essentials.hub", "Go to the hub server", "/hub");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final String hubserver = plugin.getConfig().getString("hub_server");
        if (hubserver == null || hubserver.isEmpty()) {
            sender.sendMessage(CC.t("&cInvalid hub server. Contact server admin."));
            return;
        }
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(hubserver);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
