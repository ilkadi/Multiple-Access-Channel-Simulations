package org.ehr.stats;

public class MinimalSimulationStats implements ISimulationStats {
    private final int[][] queueSizeByRoundAndStation;
    private final boolean[][] awakeByRoundAndStation;

    public MinimalSimulationStats(int executionRounds, int systemSize) {
        queueSizeByRoundAndStation = new int[executionRounds][systemSize];
        awakeByRoundAndStation = new boolean[executionRounds][systemSize];
    }

    @Override
    public int[][] getQueueSizeByRoundAndStation() {
        return queueSizeByRoundAndStation;
    }

    @Override
    public boolean[][] awakeByRoundAndStation() {
        return awakeByRoundAndStation;
    }
}
