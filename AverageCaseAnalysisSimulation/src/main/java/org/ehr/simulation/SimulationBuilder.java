package org.ehr.simulation;

import org.ehr.channel.Channel;
import org.ehr.channel.ChannelBuilder;
import org.ehr.channel.StationType;
import org.ehr.stats.ISimulationsStatRecorder;

public class SimulationBuilder {
    private double rho;
    private int systemSize;
    private int startingQueues;
    private int executionRounds;
    private String algorithmName;
    private String adversaryName;
    private StationType stationType;
    private ISimulationsStatRecorder statRecorder;

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

    public SimulationBuilder setupStatRecorder(ISimulationsStatRecorder statRecorder) {
        this.statRecorder = statRecorder;
        return this;
    }

    public SimulationBuilder setupStationType(StationType stationType) {
        this.stationType = stationType;
        return this;
    }

    public Simulation createMinimalStatSimulation() {
        Channel channel = new ChannelBuilder()
                .setSystemSize(systemSize)
                .setStartingQueues(startingQueues)
                .setupStations(stationType)
                .setupAdversary(adversaryName, rho)
                .setupAlgorithm(algorithmName)
                .setupStatRecorder(statRecorder)
                .createMinimalStatSimulation();
        return new Simulation(executionRounds, channel);
    }
}