package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.util.CC;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ItemstackCommand implements CommandExecutor {

    private boolean help(CommandSender sender, String label) {
        sender.sendMessage(CC.t("&c/" + label + " name <name>"));
        sender.sendMessage(CC.t("&c/" + label + " lore <lore>"));
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return help(sender, label);
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return true;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("name")) {
            ItemStack is = player.getInventory().getItemInHand();
            if (!validItem(is)) {
                sender.sendMessage(CC.t("&cInvalid item."));
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(i == args.length - 1 ? "" : " ");
            String name = sb.toString();
            ItemMeta meta = is.getItemMeta();
            meta.setDisplayName(CC.t(name));
            is.setItemMeta(meta);
            sender.sendMessage(CC.t("&aName changed."));
            return true;
        } else if (args[0].equalsIgnoreCase("lore")) {
            ItemStack is = player.getInventory().getItemInHand();
            if (!validItem(is)) {
                sender.sendMessage(CC.t("&cInvalid item."));
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(i == args.length - 1 ? "" : " ");
            String arg = sb.toString();
            String[] lore = arg.split("\\|");
            ItemMeta meta = is.getItemMeta();
            meta.setLore(CC.t(Arrays.asList(lore)));
            is.setItemMeta(meta);
            sender.sendMessage(CC.t("&aLore changed."));
            return true;
        }
        return true;
    }

    private boolean validItem(ItemStack item) {
        return item != null && !item.getType().equals(Material.AIR);
    }

}
