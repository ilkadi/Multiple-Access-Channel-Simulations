package org.ehr.simulation;

import org.ehr.channel.MinimalStatChannel;
import org.ehr.channel.MinimalStatChannelBuilder;

public class SimulationBuilder {
    private double rho;
    private int systemSize;
    private int startingQueues;
    private int executionRounds;
    private String algorithmName;
    private String adversaryName;

    public SimulationBuilder setExecutionRounds(int executionRounds) {
        this.executionRounds = executionRounds;
        return this;
    }

    public SimulationBuilder setStartingQueues(int startingQueues) {
        this.startingQueues = startingQueues;
        return this;
    }

    public SimulationBuilder setSystemSize(int systemSize) {
        this.systemSize = systemSize;
        return this;
    }

    public SimulationBuilder setRho(double rho) {
        this.rho = rho;
        return this;
    }

    public SimulationBuilder setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
        return this;
    }

    public SimulationBuilder setAdversaryName(String adversaryName) {
        this.adversaryName = adversaryName;
        return this;
    }

    public Simulation createMinimalStatSimulation() {
        MinimalStatChannel minimalStatChannel = new MinimalStatChannelBuilder()
                .setSystemSize(systemSize)
                .setExecutionRounds(executionRounds)
                .setStartingQueues(startingQueues)
                .setupStations()
                .setupAdversary(adversaryName, rho)
                .setupAlgorithm(algorithmName)
                .createMinimalStatSimulation();
        return new Simulation(executionRounds, minimalStatChannel);
    }
}