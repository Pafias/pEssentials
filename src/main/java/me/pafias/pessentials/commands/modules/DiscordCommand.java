package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
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
        final String link = plugin.getConfig().getString("discord_link");
        if (link == null || link.isEmpty()) {
            sender.sendMessage(CC.t("&cThere is no discord link available :("));
            return;
        }
        CommandSender target;
        if (args.length >= 1 && sender.hasPermission("essentials.discord.others"))
            target = plugin.getServer().getPlayer(args[0]);
        else target = sender;
        if (target == null) {
            sender.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        try {
            sender.sendMessage(CC.a(link)
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.openUrl(link))
                    .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(CC.a("&7&oClick to open"))));
        } catch (Throwable t) {
            final ComponentBuilder builder = new ComponentBuilder(link);
            builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.t("&7&oClick to open")).create()));
            sender.sendMessage(builder.create());
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
