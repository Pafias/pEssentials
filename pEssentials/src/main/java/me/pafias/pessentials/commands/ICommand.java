package me.pafias.pessentials.commands;

import lombok.Getter;
import me.pafias.pessentials.pEssentials;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@Getter
public abstract class ICommand implements CommandExecutor, TabExecutor {

    public final pEssentials plugin = pEssentials.get();

    private final String name, permission, description, usage;
    private final List<String> aliases;

    public ICommand(String name, String permission, String description, String usage, String... aliases) {
        this(name, permission, description, usage, Arrays.asList(aliases));
    }

    public ICommand(String name, String permission, String description, String usage, List<String> aliases) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        PluginCommand pc = plugin.getCommand(name);
        if (pc == null) return;
        pc.setDescription(description);
        if (permission != null)
            pc.setPermission(permission);
        pc.setUsage(usage);
        pc.setAliases(aliases);
        pc.setExecutor(this);
        pc.setTabCompleter(this);
    }

    public abstract void commandHandler(CommandSender sender, Command command, String label, String[] args);

    public abstract List<String> tabHandler(CommandSender sender, Command command, String label, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        commandHandler(sender, command, label, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return tabHandler(sender, command, label, args);
    }

}
