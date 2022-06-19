package org.ehr.stats.queueLatency;

public class QueueLatencyStatsExtractor {
    private final QueueLatencyByRhoStats stats;
    private final double repetitions;
    private final int executionRounds;
    private final int systemSize;

    public QueueLatencyStatsExtractor(double rho, double repetitions, int executionRounds, int systemSize) {
        this.repetitions = repetitions;
        this.executionRounds = executionRounds;
        this.systemSize = systemSize;

        this.stats = new QueueLatencyByRhoStats(rho, executionRounds);
    }

    synchronized public void appendStats(QueueLatencyStats simulationStats) {
        double totalArea = 0;

        for (int round = 0; round < executionRounds; round++) {
            double roundQueueSize = 0;
            double totalPackets = 0;

            for (int stationId = 0; stationId < systemSize; stationId++) {
                int[][] queueStats = simulationStats.getQueueSizeByRoundAndStation();
                int[][] packetStats = simulationStats.getUniquePacketsByRoundAndStation();

                roundQueueSize += queueStats[round][stationId];
                totalPackets += packetStats[round][stationId];
                //System.out.print(queueStats[round][stationId] + "\t");
            }
            totalArea += roundQueueSize;
            //System.out.println();

            stats.getSystemAverageQueue()[round] += totalArea / repetitions / (round + 1);
            stats.getSystemAverageLatency()[round] += totalArea / repetitions / totalPackets;
        }
    }

    synchronized public QueueLatencyByRhoStats getExecutorStats() {
        return stats;
    }

    synchronized public void addToLastStats(QueueLatencyLastStats lastStats) {
        int last = stats.getMaxLength() - 1;
        lastStats.getRho().add(stats.getRho());
        lastStats.getSystemAverageQueue().add(stats.getSystemAverageQueue()[last]);
        lastStats.getSystemAverageLatency().add(stats.getSystemAverageLatency()[last]);
    }
}
