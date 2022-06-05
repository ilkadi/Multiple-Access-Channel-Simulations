package org.ehr.stats;

public interface ISimulationStats {
    int[][] getQueueSizeByRoundAndStation();
    boolean[][] awakeByRoundAndStation();
}
