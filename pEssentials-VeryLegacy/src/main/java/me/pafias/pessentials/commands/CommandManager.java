package me.pafias.pessentials.commands;

import lombok.Getter;
import me.pafias.pessentials.pEssentials;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

@Getter
public class CommandManager {

    public CommandManager(pEssentials plugin) {
        commands = new HashSet<>();
        for (String command : plugin.getDescription().getCommands().keySet()) {
            if (plugin.getConfig().getStringList("disabled_commands").contains(command)) continue;
            try {
                Class<?> c = Class.forName("me.pafias.pessentials.commands.modules." + StringUtils.capitalize(command) + "Command");
                commands.add((ICommand) c.getDeclaredConstructor().newInstance());
            } catch (Throwable ex) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load command " + command, ex);
                continue;
            }
        }
    }

    private final Set<ICommand> commands;

}
