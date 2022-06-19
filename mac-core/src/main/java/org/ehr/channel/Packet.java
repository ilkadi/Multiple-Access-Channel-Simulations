package org.ehr.channel;

public class Packet {
    private final int injectionRound;

    public Packet(int injectionRound) {
        this.injectionRound = injectionRound;
    }

    public int getInjectionRound() {
        return injectionRound;
    }
}
