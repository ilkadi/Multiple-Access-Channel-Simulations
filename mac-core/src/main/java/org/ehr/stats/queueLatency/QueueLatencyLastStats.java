package org.ehr.stats.queueLatency;

import org.ehr.stats.export.IStatProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class QueueLatencyLastStats implements IStatProvider {
    private final List<Double> rho = new ArrayList<>();
    private final List<Double> systemAverageQueue = new ArrayList<>();
    private final List<Double> systemAverageLatency = new ArrayList<>();

    private final Map<String, Function<Integer, Double>> statMap = Map.of(
            "rho", (round) -> getRho().get(round),
            "sys-avg-queue", (round) -> getSystemAverageQueue().get(round),
            "sys-avg-latency", (round) -> getSystemAverageLatency().get(round)
    );

    public List<Double> getRho() {
        return rho;
    }

    public List<Double> getSystemAverageQueue() {
        return systemAverageQueue;
    }

    public List<Double> getSystemAverageLatency() {
        return systemAverageLatency;
    }

    @Override
    public int getMaxLength() {
        return rho.size();
    }

    @Override
    public List<String> getColumns() {
        return List.of("rho", "sys-avg-queue", "sys-avg-latency");
    }

    @Override
    public String getStatByColumnAndRound(String column, int round) {
        return String.format("%.3f", statMap.get(column).apply(round));
    }
}
