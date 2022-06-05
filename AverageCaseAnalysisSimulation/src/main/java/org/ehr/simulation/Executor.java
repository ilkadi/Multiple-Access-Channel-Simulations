package org.ehr.simulation;

import org.ehr.stats.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Executor {
    private final int startingQueues;
    private final int repetitions;
    private final int executionRounds;
    private final int systemSize;
    private final int threads;
    private final List<Double> rhoRange;
    private final String algorithmName;
    private final String adversaryName;
    private final IExecutorStatExporter statExporter;

    Executor(int repetitions, int executionRounds,
             int systemSize, List<Double> rhoRange,
             String algorithmName, String adversaryName,
             int threads, IExecutorStatExporter statExporter, int startingQueues) {
        this.startingQueues = startingQueues;
        this.repetitions = repetitions;
        this.executionRounds = executionRounds;
        this.systemSize = systemSize;
        this.rhoRange = rhoRange;
        this.algorithmName = algorithmName;
        this.adversaryName = adversaryName;
        this.statExporter = statExporter;
        this.threads = threads;

        System.out.println("Created: " + algorithmName + " against " + adversaryName);
    }

    public void runExperimentsForFullRhoRange() throws InterruptedException {
        ExecutionStatsHelper statsHelper = new ExecutionStatsHelper();

        for (Double rho : rhoRange) {
            ExecutionLastStats lastStats = runExperimentsForRho(rho);
            statsHelper.add(lastStats);
        }

        String outputName = "summary/" + systemSize + "/" + getOutputName();
        statsHelper.publish(outputName);
    }

    private ExecutionLastStats runExperimentsForRho(double rho) throws InterruptedException {
        System.out.println("Rho: " + rho);
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        ExecutorRhoStatsExtractor statsExtractor = new ExecutorRhoStatsExtractor(rho, repetitions, executionRounds, systemSize);

        for (int i = 0; i < repetitions; i++)
            executorService.submit(() -> runExperiment(rho, statsExtractor));

        System.out.println("Waiting for the parallel experiments to complete..");
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("Processing results..");
        ExecutorRhoStats executorRhoStats = statsExtractor.getExecutorStats();
        processSimulationStats(rho, executorRhoStats);
        return statsExtractor.getExecutionLastStats();
    }

    private void runExperiment(double rho, ExecutorRhoStatsExtractor statsExtractor) {
        ISimulationStats simulationStats = runSimulation(rho);
        statsExtractor.appendStats(simulationStats);

        System.gc();
    }

    private ISimulationStats runSimulation(double rho) {
        Simulation simulation = new SimulationBuilder()
                .setExecutionRounds(executionRounds)
                .setSystemSize(systemSize)
                .setStartingQueues(startingQueues)
                .setRho(rho)
                .setAdversaryName(adversaryName)
                .setAlgorithmName(algorithmName)
                .createMinimalStatSimulation();
        return simulation.run();
    }

    private void processSimulationStats(double rho, ExecutorRhoStats executorRhoStats) {
        String rhoString = String.format("%.3f", rho).replace(".", "");
        String outputName = getOutputName();
        statExporter.publishSummaryExecutionResultsForRho(rhoString, outputName, executorRhoStats);
    }

    private String getOutputName() {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        return algorithmName + "/" +
                String.join("_",
                        timestamp,
                        adversaryName,
                        Integer.toString(repetitions),
                        Integer.toString(executionRounds),
                        Integer.toString(systemSize))
                + ".csv";
    }
}
