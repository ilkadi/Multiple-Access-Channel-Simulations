package org.ehr.stats;

public interface IExecutorStatExporter {
    void publishSummaryExecutionResultsForRho(String rho, String outputName, ExecutorRhoStats executorRhoStats);
    void publishSummaryExecutionResults(String outputName, ExecutorRhoStats executorRhoStats);
}
