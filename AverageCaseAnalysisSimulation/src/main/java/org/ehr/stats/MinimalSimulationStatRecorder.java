package org.ehr.stats;

import org.ehr.channel.Station;

import java.util.List;

public class MinimalSimulationStatRecorder implements ISimulationsStatRecorder  {
    private final MinimalSimulationStats minimalSimulationStats;

    public MinimalSimulationStatRecorder(int executionRounds, int systemSize) {
        minimalSimulationStats = new MinimalSimulationStats(executionRounds, systemSize);
    }

    @Override
    public void recordQueueSize(int round, List<Station> stations) {
        for (Station station : stations)
            minimalSimulationStats.getQueueSizeByRoundAndStation()[round][station.getId()] = station.getQueueSize();
    }

    @Override
    public void recordAwakeStations(int round, boolean[] roundListeningStations) {
        for (int i = 0; i < roundListeningStations.length; i++)
            minimalSimulationStats.awakeByRoundAndStation()[round][i] = roundListeningStations[i];
    }

    @Override
    public ISimulationStats getSimulationStats() {
        return minimalSimulationStats;
    }
}
