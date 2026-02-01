package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.builders.SkullBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;

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
        new SkullBuilder(args[0]).buildAsync().thenAccept(skull -> {
            player.getInventory().addItem(skull);
            final OfflinePlayer owner = ((SkullMeta) skull.getItemMeta()).getOwningPlayer();
            final String name = owner != null && owner.getName() != null ? owner.getName() : args[0];
            sender.sendMessage(CC.tf("&6Received skull of &d%s", name));
        });
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        else return Collections.emptyList();
    }

}
