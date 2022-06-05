package org.ehr.ieee.communication.letters;

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

public class SimulationsSetupTest {

    @Test
    @Disabled
    public void mbtf() throws InterruptedException {
        List<Double> rhoRange = IntStream.range(1, 101)
                .asDoubleStream()
                .map(v -> 0.01 * v)
                .boxed().collect(Collectors.toList());
        CsvExecutorStatExporter statExporter = new CsvExecutorStatExporter();
        Executor executor = new ExecutorBuilder()
                .setRepetitions(128)
                .setThreads(4)
                .setStartingQueues(0)
                .setAdversaryName(Adversary.MBTF_AVERAGE.name())
                .setAlgorithmName(Algorithm.MBTF.name())
                .setExecutionRounds(1000000)
                .setSystemSize(32)
                .setRhoRange(rhoRange)
                .setExecutorStatExporter(statExporter)
                .createExecutor();
        executor.runExperimentsForFullRhoRange();
    }
}
