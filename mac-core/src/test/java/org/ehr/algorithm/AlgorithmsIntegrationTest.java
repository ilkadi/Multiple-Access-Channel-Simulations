package org.ehr.algorithm;

import org.ehr.adversary.Adversary;
import org.ehr.channel.StationType;
import org.ehr.simulation.Simulation;
import org.ehr.simulation.SimulationBuilder;
import org.ehr.stats.ISimulationsStatRecorder;
import org.ehr.stats.queueEnergy.QueueEnergyStatRecorder;
import org.ehr.stats.queueLatency.QueueLatencyStatRecorder;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AlgorithmsIntegrationTest {
    @Test
    public void shouldRunQueueEnergyWithoutExceptions() {
        int executionRounds = 500000;
        int systemSize = 32;
        ISimulationsStatRecorder statRecorder = new QueueEnergyStatRecorder(executionRounds, systemSize);
        List<Algorithm> algorithms = List.of(
                Algorithm.TWELVE_OC_ADAPTIVE,
                Algorithm.TWELVE_OC_FS,
                Algorithm.ROUND_ROBIN,
                Algorithm.STATE_AWARE,
                Algorithm.EIGHT_LIGHT_IS,
                Algorithm.BACKOFF_POL_LINEAR,
                Algorithm.BACKOFF_POL_SQUARE,
                Algorithm.BACKOFF_EXPONENTIAL);

        for (Algorithm algorithm : algorithms) {
            Simulation simulation = new SimulationBuilder()
                    .setStartingQueues(96)
                    .setAdversaryName(Adversary.STOCHASTIC_P05_B256.toString())
                    .setAlgorithmName(algorithm.name())
                    .setExecutionRounds(executionRounds)
                    .setSystemSize(systemSize)
                    .setRho(1.0)
                    .setupStationType(StationType.SIMPLE)
                    .setupStatRecorder(statRecorder)
                    .createMinimalStatSimulation();
            simulation.run();
        }
    }
    @Test
    public void shouldRunQueueLatencyEnergyWithoutExceptions() {
        int executionRounds = 50000;
        int systemSize = 32;
        ISimulationsStatRecorder statRecorder = new QueueLatencyStatRecorder(executionRounds, systemSize);
        List<Algorithm> algorithms = List.of(
                Algorithm.TWELVE_OC_ADAPTIVE,
                Algorithm.TWELVE_OC_FS,
                Algorithm.ROUND_ROBIN,
                Algorithm.STATE_AWARE,
                Algorithm.EIGHT_LIGHT_IS,
                Algorithm.BACKOFF_POL_LINEAR,
                Algorithm.BACKOFF_POL_SQUARE,
                Algorithm.BACKOFF_EXPONENTIAL);

        for (Algorithm algorithm : algorithms) {
            Simulation simulation = new SimulationBuilder()
                    .setStartingQueues(96)
                    .setAdversaryName(Adversary.STOCHASTIC_P05_B256.toString())
                    .setAlgorithmName(algorithm.name())
                    .setExecutionRounds(executionRounds)
                    .setSystemSize(systemSize)
                    .setRho(1.0)
                    .setupStationType(StationType.QUEUE_AWARE)
                    .setupStatRecorder(statRecorder)
                    .createMinimalStatSimulation();
            simulation.run();
        }
    }
    @Test
    public void shouldRunSrrWithoutExceptions() {
        int executionRounds = 100000;
        int systemSize = 64;
        ISimulationsStatRecorder statRecorder = new QueueLatencyStatRecorder(executionRounds, systemSize);
        Simulation simulation = new SimulationBuilder()
                .setStartingQueues(0)
                .setAdversaryName(Adversary.SRR_AVERAGE.toString())
                .setAlgorithmName(Algorithm.SRR.name())
                .setExecutionRounds(executionRounds)
                .setSystemSize(systemSize)
                .setRho(0.5)
                .setupStationType(StationType.QUEUE_AWARE)
                .setupStatRecorder(statRecorder)
                .createMinimalStatSimulation();
        simulation.run();
    }
}
