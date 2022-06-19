package org.ehr.stats.queueEnergy;

public class QueueEnergyStats {
    private final int[][] queueSizeByRoundAndStation;
    private final boolean[][] awakeByRoundAndStation;

    public QueueEnergyStats(int executionRounds, int systemSize) {
        queueSizeByRoundAndStation = new int[executionRounds][systemSize];
        awakeByRoundAndStation = new boolean[executionRounds][systemSize];
    }

    public int[][] getQueueSizeByRoundAndStation() {
        return queueSizeByRoundAndStation;
    }

    public boolean[][] awakeByRoundAndStation() {
        return awakeByRoundAndStation;
    }
}
