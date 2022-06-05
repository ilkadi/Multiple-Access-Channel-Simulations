package org.ehr.ieee.communication.letters;

import org.ehr.adversary.Adversary;
import org.ehr.algorithm.Algorithm;
import org.ehr.simulation.Executor;
import org.ehr.simulation.ExecutorBuilder;
import org.ehr.stats.CsvExecutorStatExporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SimulationsSetupTest {

    @Test
    @Disabled
    public void mbtf() throws InterruptedException {
        CsvExecutorStatExporter statExporter = new CsvExecutorStatExporter();
        Executor executor = new ExecutorBuilder()
                .setRepetitions(128)
                .setThreads(4)
                .setStartingQueues(0)
                .setAdversaryName(Adversary.MBTF_AVERAGE.name())
                .setAlgorithmName(Algorithm.MBTF.name())
                .setExecutionRounds(1000000)
                .setSystemSize(32)
                .setExecutorStatExporter(statExporter)
                .createExecutor();
        executor.runExperimentsForRho(0.18);
    }
}
