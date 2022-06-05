package org.ehr.stats;

import org.ehr.channel.Station;

import java.util.List;

public interface ISimulationsStatRecorder {
    ISimulationStats getSimulationStats();
    void recordQueueSize(int round, List<Station> stations);
    void recordAwakeStations(int round, boolean[] roundListeningStations);
}
