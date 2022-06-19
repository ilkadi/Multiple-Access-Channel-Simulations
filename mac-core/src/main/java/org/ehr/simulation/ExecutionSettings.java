package org.ehr.simulation;

import java.util.List;

public class ExecutionSettings {
    private final int startingQueues;
    private final int repetitions;
    private final int executionRounds;
    private final int systemSize;
    private final int threads;
    private final List<Double> rhoRange;
    private final String algorithmName;
    private final String adversaryName;

    public ExecutionSettings(int startingQueues, int repetitions, int executionRounds, int systemSize, int threads, List<Double> rhoRange, String algorithmName, String adversaryName) {
        this.startingQueues = startingQueues;
        this.repetitions = repetitions;
        this.executionRounds = executionRounds;
        this.systemSize = systemSize;
        this.threads = threads;
        this.rhoRange = rhoRange;
        this.algorithmName = algorithmName;
        this.adversaryName = adversaryName;
    }

    public int getStartingQueues() {
        return startingQueues;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getExecutionRounds() {
        return executionRounds;
    }

    public int getSystemSize() {
        return systemSize;
    }

    public int getThreads() {
        return threads;
    }

    public List<Double> getRhoRange() {
        return rhoRange;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getAdversaryName() {
        return adversaryName;
    }
}
