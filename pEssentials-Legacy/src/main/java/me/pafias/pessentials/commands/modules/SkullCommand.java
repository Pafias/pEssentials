package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.RandomUtils;
import me.pafias.putils.LCC;
import me.pafias.putils.builders.LegacySkullBuilder;
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
            sender.sendMessage(LCC.t("&c/" + label + " <player>"));
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(LCC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        new LegacySkullBuilder(args[0])
                .buildAsync().thenAccept(skull -> {
                    player.getInventory().addItem(skull);
                    sender.sendMessage(LCC.t("&6Received skull of &d" + ((SkullMeta) skull.getItemMeta()).getOwner()));
                });
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return RandomUtils.tabCompletePlayers(sender, args[0]);
        else return Collections.emptyList();
    }

}
