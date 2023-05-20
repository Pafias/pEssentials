package me.pafias.pessentials.commands;

import me.pafias.pessentials.pEssentials;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class CommandManager {

    public CommandManager(pEssentials plugin) {
        commands = new HashSet<>();
        for (String command : plugin.getDescription().getCommands().keySet()) {
            if (plugin.getSM().getVariables().disabledCommands.contains(command)) continue;
            try {
                Class<?> c = Class.forName("me.pafias.pessentials.commands.modules." + StringUtils.capitalize(command) + "Command");
                commands.add((ICommand) c.getDeclaredConstructor().newInstance());
            } catch (Exception ex) {
                continue;
            }
        }
    }

    private final Set<ICommand> commands;

    public Set<ICommand> getCommands() {
        return commands;
    }

    public ICommand getCommand(String name) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

}
