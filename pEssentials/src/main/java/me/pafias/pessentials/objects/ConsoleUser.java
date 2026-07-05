package me.pafias.pessentials.objects;

import me.pafias.putils.CC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class ConsoleUser implements Messageable {

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public String getName() {
        return "@Console";
    }

    @Override
    public void message(Component message) {
        Bukkit.getConsoleSender().sendMessage(CC.t(CC.serialize(message)));
    }

    @Override
    public boolean isBlockingPMs() {
        return false;
    }

    @Override
    public boolean isBlockingPMsFrom(Messageable sender) {
        return false;
    }

    @Override
    public boolean canBypassBlock() {
        return true;
    }

    @Override
    public boolean canBypassMsgtoggle() {
        return true;
    }

    @Override
    public boolean canColorize() {
        return true;
    }

}
