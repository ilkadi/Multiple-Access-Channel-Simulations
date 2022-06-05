package org.ehr.channel;

import org.ehr.adversary.Adversary;
import org.ehr.adversary.IAdversary;
import org.ehr.algorithm.Algorithm;
import org.ehr.algorithm.IAlgorithm;
import org.ehr.stats.ISimulationsStatRecorder;
import org.ehr.stats.MinimalSimulationStatRecorder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinimalStatChannelBuilder {
    private int systemSize;
    private int startingQueues;
    private int executionRounds;
    private List<Station> stations;
    private IAlgorithm algorithm;
    private IAdversary adversary;

    public MinimalStatChannelBuilder setExecutionRounds(int executionRounds) {
        this.executionRounds = executionRounds;
        return this;
    }

    public MinimalStatChannelBuilder setSystemSize(int systemSize) {
        this.systemSize = systemSize;
        return this;
    }

    public MinimalStatChannelBuilder setStartingQueues(int startingQueues) {
        this.startingQueues = startingQueues;
        return this;
    }

    public MinimalStatChannelBuilder setupStations() {
        this.stations = IntStream.range(0, systemSize).mapToObj(Station::new).collect(Collectors.toList());
        for (Station station : stations)
            station.setQueue(startingQueues);
        return this;
    }

    public MinimalStatChannelBuilder setupAlgorithm(String algorithmName) {
        this.algorithm = Algorithm.valueOf(algorithmName).getInstance(systemSize);
        return this;
    }

    public MinimalStatChannelBuilder setupAdversary(String adversaryName, double rho) {
        this.adversary = Adversary.valueOf(adversaryName).getInstance(rho, systemSize);
        return this;
    }

    public MinimalStatChannel createMinimalStatSimulation() {
        ISimulationsStatRecorder statRecorder = new MinimalSimulationStatRecorder(executionRounds, systemSize);
        return new MinimalStatChannel(stations, algorithm, adversary, statRecorder);
    }
}
