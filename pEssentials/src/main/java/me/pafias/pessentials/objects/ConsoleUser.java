package me.pafias.pessentials.objects;

import me.pafias.pessentials.util.CC;
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
    public void message(boolean colorize, String content) {
        Bukkit.getConsoleSender().sendMessage(CC.t(content));
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

}
