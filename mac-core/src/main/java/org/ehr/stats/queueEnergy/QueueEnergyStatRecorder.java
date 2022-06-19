package org.ehr.stats.queueEnergy;

import org.ehr.channel.IStation;
import org.ehr.stats.ISimulationsStatRecorder;

import java.util.List;

public class QueueEnergyStatRecorder implements ISimulationsStatRecorder {
    private final QueueEnergyStats queueEnergyStats;

    public QueueEnergyStatRecorder(int executionRounds, int systemSize) {
        queueEnergyStats = new QueueEnergyStats(executionRounds, systemSize);
    }

    @Override
    public void recordStationsState(int round, List<IStation> stations) {
        for (IStation station : stations)
            queueEnergyStats.getQueueSizeByRoundAndStation()[round][station.getId()] = station.getQueueSize();
    }

    @Override
    public void recordAwakeStations(int round, boolean[] roundListeningStations) {
        for (int i = 0; i < roundListeningStations.length; i++)
            queueEnergyStats.awakeByRoundAndStation()[round][i] = roundListeningStations[i];
    }

    public QueueEnergyStats getSimulationStats() {
        return queueEnergyStats;
    }
}
