package org.ehr.simulation;

import org.ehr.channel.Channel;

public class Simulation {
    private final int executionRounds;
    private final Channel channel;

    Simulation(int executionRounds, Channel channel) {
        this.executionRounds = executionRounds;
        this.channel = channel;
    }

    public void run() {
        for (int round = 0; round < executionRounds; round++)
            channel.tickRound(round);
    }
}
