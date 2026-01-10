package me.pafias.pessentials.commands.modules.Player.Socials;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.util.CC;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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
        if (args.length >= 1 && sender.hasPermission(getPermission() + ".others"))
            target = plugin.getServer().getPlayer(args[0]);
        else target = sender;
        if (target == null) {
            sender.sendMessage(CC.t("&cPlayer not found!"));
            return;
        }
        sender.sendMessage(CC.a(link)
                .clickEvent(ClickEvent.openUrl(link))
                .hoverEvent(HoverEvent.showText(CC.a("&7&oClick to open"))));
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
