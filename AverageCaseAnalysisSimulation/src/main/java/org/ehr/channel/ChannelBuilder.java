package org.ehr.channel;

import org.ehr.adversary.Adversary;
import org.ehr.adversary.IAdversary;
import org.ehr.algorithm.Algorithm;
import org.ehr.algorithm.IAlgorithm;
import org.ehr.stats.ISimulationsStatRecorder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChannelBuilder {
    private int systemSize;
    private int startingQueues;
    private ISimulationsStatRecorder statRecorder;
    private List<IStation> stations;
    private IAlgorithm algorithm;
    private IAdversary adversary;

    public ChannelBuilder setSystemSize(int systemSize) {
        this.systemSize = systemSize;
        return this;
    }

    public ChannelBuilder setStartingQueues(int startingQueues) {
        this.startingQueues = startingQueues;
        return this;
    }

    public ChannelBuilder setupStations(StationType stationType) {
        this.stations = IntStream.range(0, systemSize)
                .mapToObj(stationType::getInstance)
                .collect(Collectors.toList());
        for (IStation station : stations)
            station.setQueue(startingQueues);
        return this;
    }

    public ChannelBuilder setupAlgorithm(String algorithmName) {
        this.algorithm = Algorithm.valueOf(algorithmName).getInstance(systemSize);
        return this;
    }

    public ChannelBuilder setupAdversary(String adversaryName, double rho) {
        this.adversary = Adversary.valueOf(adversaryName).getInstance(rho, systemSize);
        return this;
    }

    public ChannelBuilder setupStatRecorder(ISimulationsStatRecorder statRecorder) {
        this.statRecorder = statRecorder;
        return this;
    }

    public Channel createMinimalStatSimulation() {
        return new Channel(stations, algorithm, adversary, statRecorder);
    }
}
