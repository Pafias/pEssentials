package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
import me.pafias.pafiasessentials.util.Reflection;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SkullCommand extends ICommand {

    public SkullCommand() {
        super("skull", "essentials.skull", "Get someone's skull", "/skull [player]", "playerhead");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("essentials.skull")) {
            if (args.length < 1) {
                sender.sendMessage(CC.t("&c/" + label + " <player>"));
                return;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.t("&cOnly players!"));
                return;
            }
            Player player = (Player) sender;
            // ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
            ItemStack skull = Reflection.getSkull();
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            getOwner(args[0]).thenAccept(owner -> {
                meta.setOwner(owner.getName());
                skull.setItemMeta(meta);
                player.getInventory().addItem(skull);
                sender.sendMessage(CC.t("&6Received skull of &d" + owner.getName()));
            });
        }
        return;
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        else return Collections.emptyList();
    }

    private CompletableFuture<OfflinePlayer> getOwner(String name) {
        CompletableFuture<OfflinePlayer> future = new CompletableFuture<>();
        future.complete(plugin.getServer().getOfflinePlayer(name));
        return future;
    }

}
