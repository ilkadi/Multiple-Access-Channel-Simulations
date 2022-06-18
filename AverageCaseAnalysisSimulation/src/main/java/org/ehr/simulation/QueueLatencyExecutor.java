package org.ehr.simulation;

import org.ehr.channel.StationType;
import org.ehr.stats.export.CsvStatExporter;
import org.ehr.stats.queueLatency.QueueLatencyByRhoStats;
import org.ehr.stats.queueLatency.QueueLatencyLastStats;
import org.ehr.stats.queueLatency.QueueLatencyStatRecorder;
import org.ehr.stats.queueLatency.QueueLatencyStatsExtractor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class QueueLatencyExecutor implements ISimulationExecutor {
    private final ExecutionSettings executionSettings;

    public QueueLatencyExecutor(ExecutionSettings executionSettings) {
        this.executionSettings = executionSettings;
    }

    @Override
    public void runExperiments() throws InterruptedException {
        QueueLatencyLastStats lastStats = new QueueLatencyLastStats();

        for (Double rho : executionSettings.getRhoRange()) {
            runExperimentsForRho(rho, lastStats);
        }

        processExecutionStats(lastStats);
    }

    private void runExperimentsForRho(double rho, QueueLatencyLastStats lastStats)
            throws InterruptedException {
        System.out.println("Rho: " + rho);
        ExecutorService executorService = Executors.newFixedThreadPool(executionSettings.getThreads());
        QueueLatencyStatsExtractor statsExtractor = new QueueLatencyStatsExtractor(rho,
                executionSettings.getRepetitions(),
                executionSettings.getExecutionRounds(),
                executionSettings.getSystemSize());

        for (int i = 0; i < executionSettings.getRepetitions(); i++)
            executorService.submit(() -> runExperiment(rho, statsExtractor));

        System.out.println("Waiting for the parallel experiments to complete..");
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        System.out.println("Processing results..");
        QueueLatencyByRhoStats stats = statsExtractor.getExecutorStats();
        processSimulationStats(rho, stats);
        statsExtractor.addToLastStats(lastStats);
    }

    private void runExperiment(double rho, QueueLatencyStatsExtractor statsExtractor) {
        QueueLatencyStatRecorder statRecorder = new QueueLatencyStatRecorder(
                executionSettings.getExecutionRounds(), executionSettings.getSystemSize());
        Simulation simulation = new SimulationBuilder()
                .setExecutionRounds(executionSettings.getExecutionRounds())
                .setSystemSize(executionSettings.getSystemSize())
                .setStartingQueues(executionSettings.getStartingQueues())
                .setRho(rho)
                .setAdversaryName(executionSettings.getAdversaryName())
                .setAlgorithmName(executionSettings.getAlgorithmName())
                .setupStationType(StationType.QUEUE_AWARE)
                .setupStatRecorder(statRecorder)
                .createMinimalStatSimulation();
        simulation.run();

        statsExtractor.appendStats(statRecorder.getSimulationStats());
        System.gc();
    }

    private void processExecutionStats(QueueLatencyLastStats queueLatencyLastStats) {
        String outputName = getOutputName();
        String subDirectoryPath = "queue_latency_summary/" + executionSettings.getSystemSize();
        CsvStatExporter summaryStatExporter = new CsvStatExporter(subDirectoryPath, 1);
        summaryStatExporter.publishSummaryExecutionResultsForRho(outputName, queueLatencyLastStats);
    }

    private void processSimulationStats(double rho, QueueLatencyByRhoStats stats) {
        String rhoString = String.format("%.3f", rho).replace(".", "");
        String outputName = getOutputName();
        String subDirectoryPath = "queue_latency/" + rhoString;
        CsvStatExporter roundStatExporter = new CsvStatExporter(subDirectoryPath, 1000);
        roundStatExporter.publishSummaryExecutionResultsForRho(outputName, stats);
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
