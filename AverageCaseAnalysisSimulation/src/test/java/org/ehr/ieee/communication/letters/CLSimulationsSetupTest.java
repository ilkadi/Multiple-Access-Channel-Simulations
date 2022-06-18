package org.ehr.ieee.communication.letters;

import org.ehr.adversary.Adversary;
import org.ehr.algorithm.Algorithm;
import org.ehr.simulation.ExecutionSettings;
import org.ehr.simulation.SimulationType;
import org.ehr.simulation.ExecutionSettingsBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLSimulationsSetupTest {

    @Test
    @Disabled
    public void mbtf() throws InterruptedException {
        List<Double> rhoRange = IntStream.range(1, 101)
                .asDoubleStream()
                .map(v -> 0.01 * v)
                .boxed().collect(Collectors.toList());
        ExecutionSettings executionSettings = new ExecutionSettingsBuilder()
                .setRepetitions(1)
                .setThreads(4)
                .setStartingQueues(0)
                .setAdversaryName(Adversary.MBTF_AVERAGE.name())
                .setAlgorithmName(Algorithm.MBTF.name())
                .setExecutionRounds(1000000)
                .setSystemSize(64)
                .setRhoRange(rhoRange)
                .createExecutionSettings();
        SimulationType.QUEUE_LATENCY
                .getInstance(executionSettings)
                .runExperiments();
    }

    @Test
    @Disabled
    public void rrw() throws InterruptedException {
        List<Double> rhoRange = IntStream.range(1, 101)
                .asDoubleStream()
                .map(v -> 0.01 * v)
                .boxed().collect(Collectors.toList());
        ExecutionSettings executionSettings = new ExecutionSettingsBuilder()
                .setRepetitions(1)
                .setThreads(4)
                .setStartingQueues(0)
                .setAdversaryName(Adversary.RRW_AVERAGE.name())
                .setAlgorithmName(Algorithm.RRW.name())
                .setExecutionRounds(1000000)
                .setSystemSize(64)
                .setRhoRange(rhoRange)
                .createExecutionSettings();
        SimulationType.QUEUE_LATENCY
                .getInstance(executionSettings)
                .runExperiments();
    }

    @Test
    @Disabled
    public void srr() throws InterruptedException {
        List<Double> rhoRange = IntStream.range(1, 101)
                .asDoubleStream()
                .map(v -> 0.01 * v)
                .boxed().collect(Collectors.toList());
        ExecutionSettings executionSettings = new ExecutionSettingsBuilder()
                .setRepetitions(1)
                .setThreads(4)
                .setStartingQueues(0)
                .setAdversaryName(Adversary.SRR_AVERAGE.name())
                .setAlgorithmName(Algorithm.SRR.name())
                .setExecutionRounds(1000000)
                .setSystemSize(64)
                .setRhoRange(rhoRange)
                .createExecutionSettings();
        SimulationType.QUEUE_LATENCY
                .getInstance(executionSettings)
                .runExperiments();
    }
}
