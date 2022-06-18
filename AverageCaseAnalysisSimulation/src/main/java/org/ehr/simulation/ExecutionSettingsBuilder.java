package org.ehr.simulation;

import java.util.List;

public class ExecutionSettingsBuilder {
    private int threads;
    private int repetitions;
    private int executionRounds;
    private int systemSize;
    private int startingQueues;
    private List<Double> rhoRange;
    private String algorithmName;
    private String adversaryName;

    public ExecutionSettingsBuilder setThreads(int threads) {
        this.threads = threads;
        return this;
    }

    public ExecutionSettingsBuilder setRepetitions(int repetitions) {
        this.repetitions = repetitions;
        return this;
    }

    public ExecutionSettingsBuilder setStartingQueues(int startingQueues) {
        this.startingQueues = startingQueues;
        return this;
    }

    public ExecutionSettingsBuilder setExecutionRounds(int executionRounds) {
        this.executionRounds = executionRounds;
        return this;
    }

    public ExecutionSettingsBuilder setSystemSize(int systemSize) {
        this.systemSize = systemSize;
        return this;
    }

    public ExecutionSettingsBuilder setRhoRange(List<Double> rhoRange) {
        this.rhoRange = rhoRange;
        return this;
    }

    public ExecutionSettingsBuilder setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
        return this;
    }

    public ExecutionSettingsBuilder setAdversaryName(String adversaryName) {
        this.adversaryName = adversaryName;
        return this;
    }
    public ExecutionSettings createExecutionSettings() {
        return new ExecutionSettings(startingQueues, repetitions, executionRounds, systemSize, threads, rhoRange, algorithmName, adversaryName);
    }
}