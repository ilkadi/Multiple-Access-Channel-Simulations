package org.ehr.simulation;

import org.ehr.stats.IExecutorStatExporter;

public class ExecutorBuilder {
    private int threads;
    private int repetitions;
    private int executionRounds;
    private int systemSize;
    private int startingQueues;
    private double rhoStep;
    private String algorithmName;
    private String adversaryName;
    private IExecutorStatExporter statRecorder;

    public ExecutorBuilder setThreads(int threads) {
        this.threads = threads;
        return this;
    }

    public ExecutorBuilder setRepetitions(int repetitions) {
        this.repetitions = repetitions;
        return this;
    }

    public ExecutorBuilder setStartingQueues(int startingQueues) {
        this.startingQueues = startingQueues;
        return this;
    }

    public ExecutorBuilder setExecutionRounds(int executionRounds) {
        this.executionRounds = executionRounds;
        return this;
    }

    public ExecutorBuilder setSystemSize(int systemSize) {
        this.systemSize = systemSize;
        return this;
    }

    public ExecutorBuilder setRhoStep(double rhoStep) {
        this.rhoStep = rhoStep;
        return this;
    }

    public ExecutorBuilder setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
        return this;
    }

    public ExecutorBuilder setAdversaryName(String adversaryName) {
        this.adversaryName = adversaryName;
        return this;
    }

    public ExecutorBuilder setExecutorStatExporter(IExecutorStatExporter statRecorder) {
        this.statRecorder = statRecorder;
        return this;
    }

    public Executor createExecutor() {
        return new Executor(repetitions, executionRounds, systemSize, rhoStep, algorithmName, adversaryName, threads, statRecorder, startingQueues);
    }
}