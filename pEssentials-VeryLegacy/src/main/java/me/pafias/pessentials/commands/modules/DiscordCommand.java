package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import me.pafias.pessentials.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class DiscordCommand extends ICommand {

    public DiscordCommand() {
        super("discord", "essentials.discord", "Show the discord invite link", "/discord", "dc");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        final String link = plugin.getConfig().getString("discord_link");
        if (link == null || link.isEmpty()) {
            sender.sendMessage(CC.t("&cThere is no discord link available :("));
            return;
        }
        Player target;
        if (args.length >= 1 && sender.hasPermission("essentials.discord.others"))
            target = plugin.getServer().getPlayer(args[0]);
        else target = (Player) sender;
        if (target == null) {
            sender.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        ChatUtil.sendClickableMessage(target, link, link);
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
