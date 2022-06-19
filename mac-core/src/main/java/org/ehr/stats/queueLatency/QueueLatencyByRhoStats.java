package org.ehr.stats.queueLatency;

import org.ehr.stats.export.IStatProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class QueueLatencyByRhoStats implements IStatProvider {
    private final double rho;
    private final double[] systemAverageQueue;
    private final double[] systemAverageLatency;
    private final Map<String, Function<Integer, Double>> statMap = Map.of(
            "sys-avg-queue", (round) -> getSystemAverageQueue()[round],
            "sys-avg-latency", (round) -> getSystemAverageLatency()[round]
    );

    public QueueLatencyByRhoStats(double rho, int executionRounds) {
        this.rho = rho;
        this.systemAverageQueue = new double[executionRounds];
        this.systemAverageLatency = new double[executionRounds];
    }

    public double getRho() {
        return rho;
    }

    public double[] getSystemAverageQueue() {
        return systemAverageQueue;
    }

    public double[] getSystemAverageLatency() {
        return systemAverageLatency;
    }

    @Override
    public List<String> getColumns() {
        return List.of("rounds", "sys-avg-queue", "sys-avg-latency");
    }

    @Override
    public String getStatByColumnAndRound(String column, int round) {
        if ("rounds".equals(column))
            return Integer.toString(round);
        return String.format("%.3f", statMap.get(column).apply(round));
    }

    @Override
    public int getMaxLength() {
        return systemAverageQueue.length;
    }
}
