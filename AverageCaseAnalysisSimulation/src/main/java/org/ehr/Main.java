package org.ehr;

import org.ehr.adversary.Adversary;
import org.ehr.algorithm.Algorithm;
import org.ehr.simulation.Executor;
import org.ehr.simulation.ExecutorBuilder;
import org.ehr.stats.IExecutorStatExporter;
import org.ehr.stats.CsvExecutorStatExporter;

import java.io.InputStream;
import java.util.Properties;

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
            double rhoStep = Double.parseDouble(
                    properties.getProperty("rhoStep", "0.1"));
            String algorithmName = properties.getProperty("algorithm", Algorithm.MBTF.name());
            String adversaryName = properties.getProperty("adversary", Adversary.STOCHASTIC_P05_B256.name());

            IExecutorStatExporter statRecorder = new CsvExecutorStatExporter();
            Executor executor = new ExecutorBuilder()
                    .setRepetitions(repetitions)
                    .setStartingQueues(startingQueues)
                    .setThreads(runThreads)
                    .setAdversaryName(adversaryName)
                    .setAlgorithmName(algorithmName)
                    .setExecutionRounds(executionRounds)
                    .setSystemSize(systemSize)
                    .setRhoStep(rhoStep)
                    .setExecutorStatExporter(statRecorder)
                    .createExecutor();

            executor.runExperimentsForFullRhoRange();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
