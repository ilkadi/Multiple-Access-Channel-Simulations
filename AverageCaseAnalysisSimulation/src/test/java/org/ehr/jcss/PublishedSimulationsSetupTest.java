package org.ehr.jcss;

import org.ehr.adversary.Adversary;
import org.ehr.algorithm.Algorithm;
import org.ehr.simulation.Executor;
import org.ehr.simulation.ExecutorBuilder;
import org.ehr.stats.CsvExecutorStatExporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/*
 * Some simulations related to: https://arxiv.org/pdf/1808.02216.pdf
 * Please note that this implementation is not the same as the one used above.
 * Hence, there are some variations in the results.
 */
public class PublishedSimulationsSetupTest {

    @Test
    @Disabled
    public void simulation12Ofs_figure6() throws InterruptedException {
        CsvExecutorStatExporter statExporter = new CsvExecutorStatExporter();
        Executor executor = new ExecutorBuilder()
                .setRepetitions(128)
                .setThreads(4)
                .setStartingQueues(96)
                .setAdversaryName(Adversary.STOCHASTIC_P05_B256.toString())
                .setAlgorithmName(Algorithm.TWELVE_OC_FS.toString())
                .setExecutionRounds(1000000)
                .setSystemSize(32)
                .setRhoStep(0.002)
                .setExecutorStatExporter(statExporter)
                .createExecutor();
        executor.runExperimentsForRho(0.968);
    }

    @Test
    @Disabled
    public void simulationsBackoff() throws InterruptedException {
        List<Algorithm> algorithms = List.of(
                Algorithm.BACKOFF_POL_LINEAR,
                Algorithm.BACKOFF_POL_SQUARE,
                Algorithm.BACKOFF_EXPONENTIAL);

        for (Algorithm algorithm : algorithms) {
            CsvExecutorStatExporter statExporter = new CsvExecutorStatExporter();
            Executor executor = new ExecutorBuilder()
                    .setRepetitions(128)
                    .setThreads(4)
                    .setStartingQueues(0)
                    .setAdversaryName(Adversary.TWO_FAVORITES_P05_B256.toString())
                    .setAlgorithmName(algorithm.toString())
                    .setExecutionRounds(10000)
                    .setSystemSize(32)
                    .setRhoStep(0.002)
                    .setExecutorStatExporter(statExporter)
                    .createExecutor();
            executor.runExperimentsForRho(0.99);
        }
    }

    @Test
    @Disabled
    public void simulationInterleavedSelectors() throws InterruptedException {
        CsvExecutorStatExporter statExporter = new CsvExecutorStatExporter();
        Executor executor = new ExecutorBuilder()
                .setRepetitions(128)
                .setThreads(4)
                .setStartingQueues(0)
                .setAdversaryName(Adversary.TWO_FAVORITES_P05_B256.toString())
                .setAlgorithmName(Algorithm.EIGHT_LIGHT_IS.toString())
                .setExecutionRounds(1000000)
                .setSystemSize(32)
                .setExecutorStatExporter(statExporter)
                .createExecutor();
        executor.runExperimentsForRho(0.18);
    }
}
