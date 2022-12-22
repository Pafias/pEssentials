package me.pafias.pafiasessentials.commands.modules;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        String hubserver = plugin.getSM().getVariables().hubServer;
        if (hubserver == null) {
            sender.sendMessage(CC.t("&cInvalid hub server. Contact server admin."));
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(hubserver);
        Player player = ((Player) sender);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
