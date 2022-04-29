package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.concurrent.CompletableFuture;

public class SkullCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public SkullCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.skull")) {
            if (args.length < 1) {
                sender.sendMessage(CC.translate("&c/" + label + " <player>"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cOnly players!"));
                return true;
            }
            Player player = (Player) sender;
            // ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
            ItemStack skull = plugin.getSM().getNMSProvider().getSkull();
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            getOwner(args[0]).thenAccept(owner -> {
                meta.setOwner(owner.getName());
                skull.setItemMeta(meta);
                player.getInventory().addItem(skull);
                sender.sendMessage(CC.translate("&6Received skull of &d" + owner.getName()));
            });
        }
        return true;
    }

    private CompletableFuture<OfflinePlayer> getOwner(String name) {
        CompletableFuture<OfflinePlayer> future = new CompletableFuture<>();
        future.complete(plugin.getServer().getOfflinePlayer(name));
        return future;
    }

}
