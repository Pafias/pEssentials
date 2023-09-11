package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemstackCommand extends ICommand {

    public ItemstackCommand() {
        super("itemstack", "essentials.itemstack", "ItemStack utilities", "/is <subcommand>", "is");
    }

    private void help(CommandSender sender, String label) {
        sender.sendMessage(CC.t("&c/" + label + " name <name>"));
        sender.sendMessage(CC.t("&c/" + label + " lore <lore>"));
        sender.sendMessage(CC.t("&c/" + label + " enchant <enchantment> <level>"));
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            help(sender, label);
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("name") || args[0].equalsIgnoreCase("rename")) {
            ItemStack is = player.getInventory().getItemInHand();
            if (!validItem(is)) {
                sender.sendMessage(CC.t("&cInvalid item."));
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(i == args.length - 1 ? "" : " ");
            String name = sb.toString();
            ItemMeta meta = is.getItemMeta();
            try {
                meta.displayName(CC.a(name));
            } catch (Exception ex) {
                meta.setDisplayName(CC.t(name));
            }
            is.setItemMeta(meta);
            sender.sendMessage(CC.t("&aName changed."));
        } else if (args[0].equalsIgnoreCase("lore")) {
            ItemStack is = player.getInventory().getItemInHand();
            if (!validItem(is)) {
                sender.sendMessage(CC.t("&cInvalid item."));
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(i == args.length - 1 ? "" : " ");
            String arg = sb.toString();
            String[] lore = arg.split("\\|");
            ItemMeta meta = is.getItemMeta();
            try {
                meta.lore(CC.a(Arrays.asList(lore)));
            } catch (Exception ex) {
                meta.setLore(CC.t(Arrays.asList(lore)));
            }
            is.setItemMeta(meta);
            sender.sendMessage(CC.t("&aLore changed."));
        } else if (args[0].equalsIgnoreCase("enchant")) {
            if (args.length < 3) {
                help(sender, label);
                return;
            }
            Enchantment enchantment;
            if (plugin.parseVersion() > 16) {
                enchantment = Enchantment.getByName(args[1].toUpperCase().trim());
                if (enchantment == null)
                    enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[1].toLowerCase().trim()));
            } else
                enchantment = Enchantment.getByName(args[1].toUpperCase().trim());
            if (enchantment == null) {
                sender.sendMessage(CC.t("&cInvalid enchantment."));
                return;
            }
            int level;
            try {
                if (args[2].equalsIgnoreCase("max"))
                    level = Integer.MAX_VALUE;
                else
                    level = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(CC.t("&cInvalid level."));
                return;
            }
            ItemStack is = player.getInventory().getItemInHand();
            if (!validItem(is)) {
                sender.sendMessage(CC.t("&cInvalid item."));
                return;
            }
            ItemMeta meta = is.getItemMeta();
            meta.addEnchant(enchantment, level, true);
            is.setItemMeta(meta);
            sender.sendMessage(CC.t("&aItem enchanted."));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return Arrays.asList("name", "lore", "enchant");
        else if (args.length == 2 && args[0].equalsIgnoreCase("enchant"))
            return Arrays
                    .stream(Enchantment.values())
                    .map(enchantment -> plugin.parseVersion() > 16 ? enchantment.getKey().getKey() : enchantment.getName())
                    .filter(e -> e.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        else return Collections.emptyList();
    }

    private boolean validItem(ItemStack item) {
        return item != null && !item.getType().equals(Material.AIR);
    }

}
