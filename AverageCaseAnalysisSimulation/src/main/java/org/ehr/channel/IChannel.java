package org.ehr.channel;

import org.ehr.stats.ISimulationStats;

import java.util.concurrent.atomic.AtomicInteger;

public interface IChannel {
    void tickRound(final int round);
    ISimulationStats getSimulationStats();
}
