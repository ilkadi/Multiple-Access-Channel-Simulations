package org.ehr.simulation;

import org.ehr.channel.StationType;
import org.ehr.stats.export.CsvStatExporter;
import org.ehr.stats.queueEnergy.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class QueueEnergyExecutor implements ISimulationExecutor {
    private final ExecutionSettings executionSettings;

    public QueueEnergyExecutor(ExecutionSettings executionSettings) {
        this.executionSettings = executionSettings;
    }

    @Override
    public void runExperiments() throws InterruptedException {
        QueueEnergyLastStats lastStats = new QueueEnergyLastStats();

        for (Double rho : executionSettings.getRhoRange()) {
            runExperimentsForRho(rho, lastStats);
        }

        processExecutionStats(lastStats);
    }

    private void runExperimentsForRho(double rho, QueueEnergyLastStats lastStats)
            throws InterruptedException {
        System.out.println("Rho: " + rho);
        ExecutorService executorService = Executors.newFixedThreadPool(executionSettings.getThreads());
        QueueEnergyByRhoStatsExtractor statsExtractor = new QueueEnergyByRhoStatsExtractor(rho,
                executionSettings.getRepetitions(),
                executionSettings.getExecutionRounds(),
                executionSettings.getSystemSize());

        for (int i = 0; i < executionSettings.getRepetitions(); i++)
            executorService.submit(() -> runExperiment(rho, statsExtractor));

        System.out.println("Waiting for the parallel experiments to complete..");
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        System.out.println("Processing results..");
        QueueEnergyByRhoStats queueEnergyByRhoStats = statsExtractor.getExecutorStats();
        processSimulationStats(rho, queueEnergyByRhoStats);
        statsExtractor.addToLastStats(lastStats);
    }

    private void runExperiment(double rho, QueueEnergyByRhoStatsExtractor statsExtractor) {
        QueueEnergyStatRecorder statRecorder = new QueueEnergyStatRecorder(
                executionSettings.getExecutionRounds(), executionSettings.getSystemSize());
        Simulation simulation = new SimulationBuilder()
                .setExecutionRounds(executionSettings.getExecutionRounds())
                .setSystemSize(executionSettings.getSystemSize())
                .setStartingQueues(executionSettings.getStartingQueues())
                .setRho(rho)
                .setAdversaryName(executionSettings.getAdversaryName())
                .setAlgorithmName(executionSettings.getAlgorithmName())
                .setupStationType(StationType.SIMPLE)
                .setupStatRecorder(statRecorder)
                .createMinimalStatSimulation();
        simulation.run();

        statsExtractor.appendStats(statRecorder.getSimulationStats());
        System.gc();
    }

    private void processExecutionStats(QueueEnergyLastStats queueEnergyLastStats) {
        String outputName = getOutputName();
        String subDirectoryPath = "queue_energy_summary/" + executionSettings.getSystemSize();
        CsvStatExporter summaryStatExporter = new CsvStatExporter(subDirectoryPath, 1);
        summaryStatExporter.publishSummaryExecutionResultsForRho(outputName, queueEnergyLastStats);
    }

    private void processSimulationStats(double rho, QueueEnergyByRhoStats queueEnergyByRhoStats) {
        String rhoString = String.format("%.3f", rho).replace(".", "");
        String outputName = getOutputName();
        String subDirectoryPath = "queue_energy/" + rhoString;
        CsvStatExporter roundStatExporter = new CsvStatExporter(subDirectoryPath, 1000);
        roundStatExporter.publishSummaryExecutionResultsForRho(outputName, queueEnergyByRhoStats);
    }

    private String getOutputName() {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        return executionSettings.getAlgorithmName() + "/" +
                String.join("_",
                        timestamp,
                        executionSettings.getAdversaryName(),
                        Integer.toString(executionSettings.getRepetitions()),
                        Integer.toString(executionSettings.getExecutionRounds()),
                        Integer.toString(executionSettings.getSystemSize()))
                + ".csv";
    }
}
