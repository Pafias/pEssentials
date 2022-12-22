package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
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
        double tps = plugin.getServer().getTPS()[0];
        tps = Math.round(tps * 1000D) / 1000D;
        sender.sendMessage(CC.t("&6Current TPS: " + (tps >= 20 ? "&2" + tps : tps < 20 && tps >= 18 ? "&a" + tps : "&c" + tps)));
        sender.sendMessage(CC.t("&6Maximum memory: &c" + (Runtime.getRuntime().maxMemory() / 1024L / 1024L) + " MB &6(&c" + (Runtime.getRuntime().maxMemory() / 1024L / 1024L / 1024L) + " GB&6)"));
        sender.sendMessage(CC.t("&6Total memory: &c" + (Runtime.getRuntime().totalMemory() / 1024L / 1024L) + " MB &6(&c" + (Runtime.getRuntime().totalMemory() / 1024L / 1024L / 1024L) + " GB&6)"));
        sender.sendMessage(CC.t("&6Free memory: &c" + (Runtime.getRuntime().freeMemory() / 1024L / 1024L) + " MB &6(&c" + (Runtime.getRuntime().freeMemory() / 1024L / 1024L / 1024L) + " GB&6)"));
        plugin.getServer().getWorlds().forEach(world -> sender.sendMessage(CC.t("&6World \"&c" + world.getName() + "&6\": &c" + world.getLoadedChunks().length + "&6/&c" + world.getChunkCount() + " &6chunks loaded, &c" + world.getEntityCount() + " &6entities, &c" + world.getTickableTileEntityCount() + "&6/&c" + world.getTileEntityCount() + " &6tile entities ticking.")));
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
