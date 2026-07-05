package me.pafias.pessentials.commands.modules;

import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.putils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SocialspyCommand extends ICommand {

    public SocialspyCommand() {
        super("socialspy", "essentials.socialspy", "Spy on private messages", "/socialspy");
    }

    @Override
    public void commandHandler(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player playerSender)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final User player = plugin.getSM().getUserManager().getUser(playerSender);
        if (player.isSpyingDms()) {
            player.setSpyingDms(false);
            playerSender.sendMessage(CC.t("&6SocialSpy: &cOFF"));
        } else {
            player.setSpyingDms(true);
            playerSender.sendMessage(CC.t("&6SocialSpy: &aON"));
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
