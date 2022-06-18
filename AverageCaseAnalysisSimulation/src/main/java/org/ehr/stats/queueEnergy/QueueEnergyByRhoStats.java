package org.ehr.stats.queueEnergy;

import org.ehr.stats.export.IStatProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class QueueEnergyByRhoStats implements IStatProvider {
    private final double rho;
    private final double[] maxMax;
    private final double[] avgMax;
    private final double[] maxAvg;
    private final double[] avgAvg;
    private final double[] maxEnergy;
    private final double[] avgEnergy;
    private final Map<String, Function<Integer, Double>> statMap = Map.of(
            "max-max", (round) -> getMaxMax()[round],
            "max-avg", (round) -> getMaxAvg()[round],
            "avg-max", (round) -> getAvgMax()[round],
            "avg-avg", (round) -> getAvgAvg()[round],
            "max-energy", (round) -> getMaxEnergy()[round],
            "avg-energy", (round) -> getAvgEnergy()[round]
    );

    public QueueEnergyByRhoStats(double rho, int executionRounds) {
        this.rho = rho;
        this.maxMax = new double[executionRounds];
        this.avgMax = new double[executionRounds];
        this.maxAvg = new double[executionRounds];
        this.avgAvg = new double[executionRounds];
        this.maxEnergy = new double[executionRounds];
        this.avgEnergy = new double[executionRounds];
    }

    public double getRho() {
        return rho;
    }

    public double[] getMaxMax() {
        return maxMax;
    }

    public double[] getAvgMax() {
        return avgMax;
    }

    public double[] getMaxAvg() {
        return maxAvg;
    }

    public double[] getAvgAvg() {
        return avgAvg;
    }

    public double[] getMaxEnergy() {
        return maxEnergy;
    }

    public double[] getAvgEnergy() {
        return avgEnergy;
    }

    @Override
    public List<String> getColumns() {
        return List.of("rounds", "max-max", "max-avg", "avg-max", "avg-avg", "max-energy", "avg-energy");
    }

    @Override
    public String getStatByColumnAndRound(String column, int round) {
        if ("rounds".equals(column))
            return Integer.toString(round);
        return String.format("%.3f", statMap.get(column).apply(round));
    }

    @Override
    public int getMaxLength() {
        return maxMax.length;
    }
}
