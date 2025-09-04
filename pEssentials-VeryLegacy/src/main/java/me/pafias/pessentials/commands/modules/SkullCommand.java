package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;

public class SkullCommand extends ICommand {

    public SkullCommand() {
        super("skull", "essentials.skull", "Get someone's skull", "/skull [player]", "playerhead");
    }

    @Override
    public void commandHandler(final CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.t("&c/" + label + " <player>"));
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final Player player = (Player) sender;
        final ItemStack skull = new ItemStack(397, 1, (short) SkullType.PLAYER.ordinal());
        final SkullMeta meta = (SkullMeta) skull.getItemMeta();
        getOwner(args[0], new OwnerCallback() {
            @Override
            public void onOwnerFound(OfflinePlayer owner) {
                meta.setOwner(owner.getName());
                skull.setItemMeta(meta);
                player.getInventory().addItem(skull);
                sender.sendMessage(CC.t("&6Received skull of &d" + owner.getName()));
            }
        });
    }

    private void getOwner(String name, OwnerCallback callback) {
        callback.onOwnerFound(plugin.getServer().getOfflinePlayer(name));
    }

    private interface OwnerCallback {
        void onOwnerFound(OfflinePlayer owner);
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> names = new java.util.ArrayList<>();
            String prefix = args[0].toLowerCase();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (((Player) sender).canSee(p)) {
                    String n = p.getName();
                    if (n != null && n.toLowerCase().startsWith(prefix))
                        names.add(n);
                }
            }
            return names;
        } else return Collections.emptyList();
    }

}
