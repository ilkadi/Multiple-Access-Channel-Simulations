package org.ehr.jcss;

import org.ehr.adversary.Adversary;
import org.ehr.algorithm.Algorithm;
import org.ehr.simulation.Executor;
import org.ehr.simulation.ExecutorBuilder;
import org.ehr.stats.CsvExecutorStatExporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Some simulations related to: https://arxiv.org/pdf/1808.02216.pdf
 * Please note that this implementation is not the same as the one used above.
 * Hence, there are some variations in the results.
 */
public class PublishedSimulationsSetupTest {
    private static final int DEFAULT_REPETITIONS = 128;
    private static final int ONE_MILLION = 1000000;

    @Test
    @Disabled
    public void simulation12Ofs_figure6() throws InterruptedException {
        CsvExecutorStatExporter statExporter = new CsvExecutorStatExporter();
        Executor executor = new ExecutorBuilder()
                .setRepetitions(DEFAULT_REPETITIONS)
                .setThreads(4)
                .setStartingQueues(96)
                .setAdversaryName(Adversary.STOCHASTIC_P05_B256.toString())
                .setAlgorithmName(Algorithm.TWELVE_OC_ADAPTIVE.name())
                .setExecutionRounds(ONE_MILLION)
                .setSystemSize(32)
                .setRhoRange(List.of(0.968))
                .setExecutorStatExporter(statExporter)
                .createExecutor();
        executor.runExperimentsForFullRhoRange();
    }

    @Test
    @Disabled
    public void simulations_figures7And8() throws InterruptedException {
        List<Algorithm> algorithms = List.of(
                Algorithm.TWELVE_OC_ADAPTIVE,
                Algorithm.TWELVE_OC_FS,
                Algorithm.ROUND_ROBIN,
                Algorithm.STATE_AWARE,
                Algorithm.EIGHT_LIGHT_IS,
                Algorithm.BACKOFF_POL_LINEAR,
                Algorithm.BACKOFF_POL_SQUARE,
                Algorithm.BACKOFF_EXPONENTIAL);
        List<Integer> systemSizes = IntStream.range(3, 33).boxed().collect(Collectors.toList());
        List<Double> rhoRange = IntStream.range(1, 101)
                .asDoubleStream()
                .map(v -> 0.01 * v)
                .boxed().collect(Collectors.toList());

        for (Integer systemSize : systemSizes) {
            for (Algorithm algorithm : algorithms) {
                CsvExecutorStatExporter statExporter = new CsvExecutorStatExporter();
                Executor executor = new ExecutorBuilder()
                        .setRepetitions(DEFAULT_REPETITIONS)
                        .setThreads(4)
                        .setStartingQueues(0)
                        .setAdversaryName(Adversary.TWO_FAVORITES_P05_B256.toString())
                        .setAlgorithmName(algorithm.toString())
                        .setExecutionRounds(ONE_MILLION)
                        .setSystemSize(systemSize)
                        .setRhoRange(rhoRange)
                        .setExecutorStatExporter(statExporter)
                        .createExecutor();
                executor.runExperimentsForFullRhoRange();
            }
        }
    }
}
