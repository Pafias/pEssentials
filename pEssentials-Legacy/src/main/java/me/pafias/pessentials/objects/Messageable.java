package me.pafias.pessentials.objects;

public interface Messageable {

    String getName();

    void message(boolean colorize, String content);

    boolean isBlockingPMs();
    boolean isBlockingPMsFrom(Messageable sender);

    boolean canBypassBlock();
    boolean canBypassMsgtoggle();

}
