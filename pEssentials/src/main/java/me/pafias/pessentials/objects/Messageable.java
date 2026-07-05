package me.pafias.pessentials.objects;

import net.kyori.adventure.text.Component;

public interface Messageable {
    boolean isOnline();
    String getName();

    void message(Component message);

    boolean isBlockingPMs();
    boolean isBlockingPMsFrom(Messageable sender);

    boolean canBypassBlock();
    boolean canBypassMsgtoggle();

    boolean canColorize();
}
