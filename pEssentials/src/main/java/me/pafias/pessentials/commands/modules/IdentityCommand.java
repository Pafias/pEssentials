package me.pafias.pessentials.commands.modules;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.pafias.pessentials.commands.ICommand;
import me.pafias.pessentials.objects.User;
import me.pafias.pessentials.util.CC;
import me.pafias.putils.Tasks;
import me.pafias.putils.builders.PlayerProfileBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class IdentityCommand extends ICommand {

    public IdentityCommand() {
        super("identity", "essentials.identity", "Change your identity (disguise)", "/id <reset/<player>> [skin]", "id");
    }

    @Override
    public void commandHandler(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.t("&c/" + label + " <reset/<name>> [skin]"));
            sender.sendMessage(CC.t("&6[skin] = the player who's skin you want to have"));
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CC.t("&cOnly players!"));
            return;
        }
        final User user = plugin.getSM().getUserManager().getUser(player.getUniqueId());
        if (args[0].equalsIgnoreCase("reset")) {
            if (!user.hasIdentity()) {
                sender.sendMessage(CC.t("&cYou are already yourself"));
                return;
            }
            user.setIdentity(user.getOriginalGameProfile());
            sender.sendMessage(CC.t("&aIdentity restored."));
        } else {
            boolean hasSkin = args.length == 2;
            final String name = args[0];
            if (name.length() > 16) {
                sender.sendMessage(CC.t("&cName cannot be longer than 16 characters."));
                return;
            }
            String skin = name;
            if (hasSkin)
                skin = args[1];
            if (skin.length() > 16) {
                sender.sendMessage(CC.t("&cName of skin player cannot be longer than 16 characters."));
                return;
            }
            sender.sendMessage(CC.t("&aProcessing..."));
            try {
                CompletableFuture<PlayerProfile> skinProfileFuture;
                if (skin.equals(user.getOriginalGameProfile().getName()))
                    skinProfileFuture = CompletableFuture.completedFuture(user.getOriginalGameProfile());
                else
                    skinProfileFuture = new PlayerProfileBuilder()
                            .setName(skin)
                            .setGenerateUuidFromName(false)
                            .setFetchProperties(true)
                            .buildAsync();

                skinProfileFuture
                        .thenCompose(skinProfile -> new PlayerProfileBuilder()
                                .setProperties(skinProfile.getProperties())
                                .setUuid(user.getUUID())
                                .setName(name)
                                .buildAsync())
                        .thenAccept(fakeProfile -> {
                            Tasks.runSync(() -> {
                                user.setIdentity(fakeProfile);
                                sender.sendMessage(CC.t("&aIdentity changed."));
                            });
                        });
            } catch (Exception ex) {
                sender.sendMessage(CC.t("&cSomething went wrong. &oDoes that player exist?"));
            }
        }
    }

    @Override
    public List<String> tabHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("reset");
            list.addAll(plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList()
            );
            return list;
        } else if (args.length == 2)
            return plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(p -> ((Player) sender).canSee(p))
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        else return Collections.emptyList();
    }

}
