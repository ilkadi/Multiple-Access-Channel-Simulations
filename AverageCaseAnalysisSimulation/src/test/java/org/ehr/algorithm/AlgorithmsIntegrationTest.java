package org.ehr.algorithm;

import org.ehr.adversary.Adversary;
import org.ehr.simulation.Simulation;
import org.ehr.simulation.SimulationBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AlgorithmsIntegrationTest {
    @Test
    public void shouldRUnWithoutExceptions() {
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
                    .setExecutionRounds(500000)
                    .setSystemSize(32)
                    .setRho(1.0)
                    .createMinimalStatSimulation();
            simulation.run();
        }
    }
}
