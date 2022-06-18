package org.ehr.stats.queueLatency;

import org.ehr.channel.IStation;
import org.ehr.channel.PacketAwareStation;
import org.ehr.stats.ISimulationsStatRecorder;

import java.util.List;

public class QueueLatencyStatRecorder implements ISimulationsStatRecorder {
    private final QueueLatencyStats queueLatencyStats;

    public QueueLatencyStatRecorder(int executionRounds, int systemSize) {
        queueLatencyStats = new QueueLatencyStats(executionRounds, systemSize);
    }

    @Override
    public void recordStationsState(int round, List<IStation> stations) {
        for (IStation station : stations) {
            int id = station.getId();
            PacketAwareStation packetAwareStation = (PacketAwareStation) station;
            queueLatencyStats.getQueueSizeByRoundAndStation()[round][id] =
                    packetAwareStation.getQueueSize();
            queueLatencyStats.getUniquePacketsByRoundAndStation()[round][id] =
                    packetAwareStation.getUniquePacketCount();
        }
    }

    @Override
    public void recordAwakeStations(int round, boolean[] roundListeningStations) {
    }

    public QueueLatencyStats getSimulationStats() {
        return queueLatencyStats;
    }
}
