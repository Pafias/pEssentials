package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class LagCommand extends ICommand {

    public LagCommand() {
        super("lag", "essentials.lag", "Server performance information", "/lag", "performance", "gc", "mem", "memory");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(CC.t("&f------- &6Server Performance &f-------"));
        sender.sendMessage(CC.t("&6Maximum memory: &c" + (Runtime.getRuntime().maxMemory() / 1024L / 1024L) + " MB &6(&c" + (Runtime.getRuntime().maxMemory() / 1024L / 1024L / 1024L) + " GB&6)"));
        sender.sendMessage(CC.t("&6Total memory: &c" + (Runtime.getRuntime().totalMemory() / 1024L / 1024L) + " MB &6(&c" + (Runtime.getRuntime().totalMemory() / 1024L / 1024L / 1024L) + " GB&6)"));
        sender.sendMessage(CC.t("&6Free memory: &c" + (Runtime.getRuntime().freeMemory() / 1024L / 1024L) + " MB &6(&c" + (Runtime.getRuntime().freeMemory() / 1024L / 1024L / 1024L) + " GB&6)"));
        for (World world : plugin.getServer().getWorlds()) {
            sender.sendMessage(CC.t("&6World \"&c" + world.getName() + "&6\": &c" + world.getLoadedChunks().length + "&6/&c" + world.getLoadedChunks().length + " &6chunks loaded, &c" + world.getEntities().size() + " &6entities."));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
