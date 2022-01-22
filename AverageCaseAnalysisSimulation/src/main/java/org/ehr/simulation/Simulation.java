package org.ehr.simulation;

import org.ehr.adversary.MbtfAdversary;
import org.ehr.adversary.RrwAdversary;
import org.ehr.adversary.SrrAdversary;
import org.ehr.adversary.StochasticAdversary;
import org.ehr.algorithm.MoveBigToFront;
import org.ehr.algorithm.RoundRobinWithholding;
import org.ehr.channel.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final IAlgorithm algorithm;
    private final int executionRounds;
    private Channel channel;
    private StatRecorder statRecorder;
    private IAdversary adversary;

    public Simulation(IAlgorithm algorithm, int executionRounds) {
        this.algorithm = algorithm;
        this.executionRounds = executionRounds;
    }

    public void execute(double rho, int systemSize) {
        int repetitions = 1;
        List<Double> avgQueueList = new ArrayList<>(repetitions);
        List<Double> avgLatencyList = new ArrayList<>(repetitions);

        for (int i = 0; i < repetitions; i++) {
            algorithm.reset();
            createChannel(rho, systemSize);
            for (int round = 0; round < executionRounds; round++) {
                adversary.tickRound();
                channel.executeRound(round);
                statRecorder.processRoundStats(round);
            }

            avgQueueList.add(statRecorder.getAverageQueue());
            avgLatencyList.add(statRecorder.getAverageLatency());
            statRecorder.reset();
        }

        System.out.println(
                avgQueueList.stream()
                        .mapToDouble(Double::doubleValue)
                        .average().getAsDouble() + "\t\t" +
                        avgLatencyList.stream()
                                .mapToDouble(Double::doubleValue)
                                .average().getAsDouble());
    }

    private void createChannel(double rho, int systemSize) {
        List<Station> stations = new ArrayList<>(systemSize);

        for (int id = 0; id < systemSize; id++) {
            Station s = new Station(algorithm, id);
            stations.add(s);
        }

        // adversary = new StochasticAdversary(rho, systemSize);
        adversary = getAdversaryForAlgorithm(algorithm, systemSize, rho);
        statRecorder = new StatRecorder(stations);
        channel = new Channel(stations, algorithm, adversary, statRecorder);
    }

    private IAdversary getAdversaryForAlgorithm(IAlgorithm algorithm, int systemSize, double rho) {
        if (algorithm instanceof RoundRobinWithholding)
            return new RrwAdversary(rho, systemSize);
        else if (algorithm instanceof MoveBigToFront)
            return new MbtfAdversary(rho, systemSize);
        else
            return new SrrAdversary(rho, systemSize);
    }
}
