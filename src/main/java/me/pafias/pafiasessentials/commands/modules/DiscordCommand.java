package me.pafias.pafiasessentials.commands.modules;

import me.pafias.pafiasessentials.commands.ICommand;
import me.pafias.pafiasessentials.util.CC;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class DiscordCommand extends ICommand {

    public DiscordCommand() {
        super("discord", "essentials.discord", "Show the discord invite link", "/discord", "dc");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        String link = plugin.getSM().getVariables().discordLink;
        ComponentBuilder builder = new ComponentBuilder(link);
        builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.t("&7&oClick to open")).create()));
        CommandSender target;
        if (args.length >= 1 && sender.hasPermission("essentials.discord.others"))
            target = plugin.getServer().getPlayer(args[0]);
        else target = sender;
        if (target == null) {
            sender.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        sender.sendMessage(builder.create());
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
