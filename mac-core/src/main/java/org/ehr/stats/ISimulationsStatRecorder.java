package org.ehr.stats;

import org.ehr.channel.IStation;

import java.util.List;

public interface ISimulationsStatRecorder {
    void recordStationsState(int round, List<IStation> stations);

    void recordAwakeStations(int round, boolean[] roundListeningStations);
}
