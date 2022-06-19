package org.ehr.stats.queueEnergy;

public class QueueEnergyByRhoStatsExtractor {
    private final QueueEnergyByRhoStats queueEnergyByRhoStats;
    private final double repetitions;
    private final int executionRounds;
    private final int systemSize;

    public QueueEnergyByRhoStatsExtractor(double rho, double repetitions, int executionRounds, int systemSize) {
        this.repetitions = repetitions;
        this.executionRounds = executionRounds;
        this.systemSize = systemSize;

        this.queueEnergyByRhoStats = new QueueEnergyByRhoStats(rho, executionRounds);
    }

    synchronized public void appendStats(QueueEnergyStats simulationStats) {
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

            queueEnergyByRhoStats.getMaxMax()[round] = Math.max(queueEnergyByRhoStats.getMaxMax()[round], maxMax);
            queueEnergyByRhoStats.getMaxAvg()[round] = Math.max(queueEnergyByRhoStats.getMaxAvg()[round], maxAvg);
            queueEnergyByRhoStats.getAvgMax()[round] += avgMax / repetitions / (round + 1.0);
            queueEnergyByRhoStats.getAvgAvg()[round] += avgAvg / repetitions / (round + 1.0);

            queueEnergyByRhoStats.getMaxEnergy()[round] = Math.max(queueEnergyByRhoStats.getMaxEnergy()[round], maxEnergy);
            queueEnergyByRhoStats.getAvgEnergy()[round] += avgEnergy / repetitions / (round + 1.0);
        }
    }

    synchronized public QueueEnergyByRhoStats getExecutorStats() {
        return queueEnergyByRhoStats;
    }

    synchronized public void addToLastStats(QueueEnergyLastStats lastStats) {
        int last = queueEnergyByRhoStats.getMaxMax().length - 1;
        lastStats.getRho().add(queueEnergyByRhoStats.getRho());
        lastStats.getMaxMax().add(queueEnergyByRhoStats.getMaxMax()[last]);
        lastStats.getAvgMax().add(queueEnergyByRhoStats.getAvgMax()[last]);
        lastStats.getMaxAvg().add(queueEnergyByRhoStats.getMaxAvg()[last]);
        lastStats.getAvgAvg().add(queueEnergyByRhoStats.getAvgAvg()[last]);
        lastStats.getMaxEnergy().add(queueEnergyByRhoStats.getMaxEnergy()[last]);
        lastStats.getAvgEnergy().add(queueEnergyByRhoStats.getAvgEnergy()[last]);
    }
}
