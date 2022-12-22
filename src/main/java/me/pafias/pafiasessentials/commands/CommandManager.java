package me.pafias.pafiasessentials.commands;

import me.pafias.pafiasessentials.PafiasEssentials;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class CommandManager {

    private final PafiasEssentials plugin;

    public CommandManager(PafiasEssentials plugin) {
        this.plugin = plugin;
        commands = new HashSet<>();
        for (String command : plugin.getDescription().getCommands().keySet()) {
            try {
                Class c = Class.forName("me.pafias.pafiasessentials.commands.modules." + StringUtils.capitalize(command) + "Command");
                if (c == null) continue;
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
