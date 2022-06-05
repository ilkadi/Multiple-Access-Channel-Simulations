package org.ehr.stats;

public class ExecutorStatsExtractor {
    private final ExecutorRhoStats executorRhoStats;
    private final double repetitions;
    private final int executionRounds;
    private final int systemSize;

    public ExecutorStatsExtractor(double rho, double repetitions, int executionRounds, int systemSize) {
        this.repetitions = repetitions;
        this.executionRounds = executionRounds;
        this.systemSize = systemSize;

        this.executorRhoStats = new ExecutorRhoStats(rho, executionRounds);
    }

    synchronized public void appendStats(ISimulationStats simulationStats) {
        double maxMax = 0;
        double maxAvg = 0;
        double avgMax = 0;
        double avgAvg = 0;
        double maxEnergy = 0;
        double avgEnergy = 0;

        for (int round = 0; round < executionRounds; round++) {
            double roundMax = 0;
            double roundAvg = 0;
            double roundAvgEnergy = 0;

            for (int stationId = 0; stationId < systemSize; stationId++) {
                int[][] queueStats = simulationStats.getQueueSizeByRoundAndStation();
                boolean[][] awakeStats = simulationStats.awakeByRoundAndStation();

                roundMax = Math.max(queueStats[round][stationId], roundMax);
                roundAvg += queueStats[round][stationId];
                roundAvgEnergy += awakeStats[round][stationId] ? 1.0 : 0.0;
                //System.out.print(queueStats[round][stationId] + "\t");
            }
            //System.out.println();
            maxEnergy = Math.max(maxEnergy, roundAvgEnergy);
            roundAvg /= systemSize;

            maxMax = Math.max(maxMax, roundMax);
            maxAvg = Math.max(maxAvg, roundAvg);
            avgMax += roundMax;
            avgAvg += roundAvg;
            avgEnergy += roundAvgEnergy;

            executorRhoStats.getMaxMax()[round] = Math.max(executorRhoStats.getMaxMax()[round], maxMax);
            executorRhoStats.getMaxAvg()[round] = Math.max(executorRhoStats.getMaxAvg()[round], maxAvg);
            executorRhoStats.getAvgMax()[round] += avgMax / repetitions / (round + 1.0);
            executorRhoStats.getAvgAvg()[round] += avgAvg / repetitions / (round + 1.0);

            executorRhoStats.getMaxEnergy()[round] = Math.max(executorRhoStats.getMaxEnergy()[round], maxEnergy);
            executorRhoStats.getAvgEnergy()[round] += avgEnergy / repetitions / (round + 1.0);
        }
    }

    synchronized public ExecutorRhoStats getExecutorStats() {
        return executorRhoStats;
    }
}
