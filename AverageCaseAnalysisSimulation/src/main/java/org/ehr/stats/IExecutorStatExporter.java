package org.ehr.stats;

public interface IExecutorStatExporter {
    void publishSummaryExecutionResultsForRho(String rho, String outputName, ExecutorRhoStats executorRhoStats);
}
