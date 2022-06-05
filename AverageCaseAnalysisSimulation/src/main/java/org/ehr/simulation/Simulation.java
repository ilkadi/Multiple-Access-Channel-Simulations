package org.ehr.simulation;

import org.ehr.channel.IChannel;
import org.ehr.stats.ISimulationStats;

public class Simulation {
    private final int executionRounds;
    private final IChannel channel;

    Simulation(int executionRounds, IChannel channel) {
        this.executionRounds = executionRounds;
        this.channel = channel;
    }

    public ISimulationStats run() {
        for (int round = 0; round < executionRounds; round++)
            channel.tickRound(round);
        return channel.getSimulationStats();
    }
}
