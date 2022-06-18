package org.ehr.stats.queueEnergy;

import org.ehr.stats.export.IStatProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class QueueEnergyLastStats implements IStatProvider {
    private final List<Double> rho = new ArrayList<>();
    private final List<Double> maxMax = new ArrayList<>();
    private final List<Double> avgMax = new ArrayList<>();
    private final List<Double> maxAvg = new ArrayList<>();
    private final List<Double> avgAvg = new ArrayList<>();
    private final List<Double> maxEnergy = new ArrayList<>();
    private final List<Double> avgEnergy = new ArrayList<>();

    private final Map<String, Function<Integer, Double>> statMap = Map.of(
            "rho", (round) -> getRho().get(round),
            "max-max", (round) -> getMaxMax().get(round),
            "max-avg", (round) -> getMaxAvg().get(round),
            "avg-max", (round) -> getAvgMax().get(round),
            "avg-avg", (round) -> getAvgAvg().get(round),
            "max-energy", (round) -> getMaxEnergy().get(round),
            "avg-energy", (round) -> getAvgEnergy().get(round)
    );

    public List<Double> getRho() {
        return rho;
    }

    public List<Double> getMaxMax() {
        return maxMax;
    }

    public List<Double> getAvgMax() {
        return avgMax;
    }

    public List<Double> getMaxAvg() {
        return maxAvg;
    }

    public List<Double> getAvgAvg() {
        return avgAvg;
    }

    public List<Double> getMaxEnergy() {
        return maxEnergy;
    }

    public List<Double> getAvgEnergy() {
        return avgEnergy;
    }

    @Override
    public int getMaxLength() {
        return rho.size();
    }

    @Override
    public List<String> getColumns() {
        return List.of("rho", "max-max", "max-avg", "avg-max", "avg-avg", "max-energy", "avg-energy");
    }

    @Override
    public String getStatByColumnAndRound(String column, int round) {
        return String.format("%.3f", statMap.get(column).apply(round));
    }
}
