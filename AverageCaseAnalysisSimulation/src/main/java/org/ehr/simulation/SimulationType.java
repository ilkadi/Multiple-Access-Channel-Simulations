package org.ehr.simulation;

import java.util.function.Function;

public enum SimulationType {
    QUEUE_ENERGY(QueueEnergyExecutor::new),
    QUEUE_LATENCY(QueueLatencyExecutor::new);

    private final Function<ExecutionSettings, ISimulationExecutor> simulationExecutorCreator;

    SimulationType(Function<ExecutionSettings, ISimulationExecutor> simulationExecutorCreator) {
        this.simulationExecutorCreator = simulationExecutorCreator;
    }

    public ISimulationExecutor getInstance(ExecutionSettings executionSettings) {
        return simulationExecutorCreator.apply(executionSettings);
    }
}
