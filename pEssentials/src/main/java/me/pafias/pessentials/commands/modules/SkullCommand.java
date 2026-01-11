package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.putils.builders.SkullBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkullCommand extends ICommand {

    public SkullCommand() {
        super("skull", "essentials.skull", "Get someone's skull", "/skull [player]", "playerhead");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.t("&c/" + label + " <player>"));
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        CompletableFuture.supplyAsync(() -> new SkullBuilder(args[0]).build())
                .thenAccept(skull -> {
                    player.getInventory().addItem(skull);
                    sender.sendMessage(CC.tf("&6Received skull of &d%s", ((SkullMeta) skull.getItemMeta()).getOwningPlayer().getName()));
                });
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(p -> p.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        else return Collections.emptyList();
    }

}
