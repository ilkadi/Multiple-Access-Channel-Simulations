package org.ehr.stats.queueLatency;

public class QueueLatencyStats {
    private final int[][] queueSizeByRoundAndStation;
    private final int[][] uniquePacketsByRoundAndStation;

    public QueueLatencyStats(int executionRounds, int systemSize) {
        queueSizeByRoundAndStation = new int[executionRounds][systemSize];
        uniquePacketsByRoundAndStation = new int[executionRounds][systemSize];
    }

    public int[][] getQueueSizeByRoundAndStation() {
        return queueSizeByRoundAndStation;
    }

    public int[][] getUniquePacketsByRoundAndStation() {
        return uniquePacketsByRoundAndStation;
    }
}
