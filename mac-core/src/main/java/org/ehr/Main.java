package org.ehr;

import org.ehr.adversary.Adversary;
import org.ehr.algorithm.Algorithm;
import org.ehr.simulation.ExecutionSettings;
import org.ehr.simulation.SimulationType;
import org.ehr.simulation.ExecutionSettingsBuilder;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            int repetitions = Integer.parseInt(
                    properties.getProperty("repetitions", "64"));
            int runThreads = Integer.parseInt(
                    properties.getProperty("threads", "2"));
            int executionRounds = Integer.parseInt(
                    properties.getProperty("executionRounds", "1000000"));
            int systemSize = Integer.parseInt(
                    properties.getProperty("systemSize", "32"));
            int startingQueues = Integer.parseInt(
                    properties.getProperty("startingQueues", "0"));
            int rhoSantiMax = Integer.parseInt(
                    properties.getProperty("rhoSantiMax", "100"));
            String algorithmName = properties.getProperty("algorithm", Algorithm.MBTF.name());
            String adversaryName = properties.getProperty("adversary", Adversary.STOCHASTIC_P05_B256.name());

            List<Double> rhoRange = IntStream.range(1, rhoSantiMax + 1)
                    .asDoubleStream()
                    .map(v -> 0.01 * v)
                    .boxed().collect(Collectors.toList());
            ExecutionSettings executionSettings = new ExecutionSettingsBuilder()
                    .setRepetitions(repetitions)
                    .setStartingQueues(startingQueues)
                    .setThreads(runThreads)
                    .setAdversaryName(adversaryName)
                    .setAlgorithmName(algorithmName)
                    .setExecutionRounds(executionRounds)
                    .setSystemSize(systemSize)
                    .setRhoRange(rhoRange)
                    .createExecutionSettings();
            SimulationType.QUEUE_ENERGY
                    .getInstance(executionSettings)
                    .runExperiments();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
