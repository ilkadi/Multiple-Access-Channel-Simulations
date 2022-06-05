package org.ehr.algorithm;

public class StateAware implements IAlgorithm {
    private final int systemSize;
    private int lastRound = -1;
    private int nextRoundQueueMax = 0;
    private int nextRoundTarget = 0;
    private int currentRoundTarget = 0;

    public StateAware(int systemSize) {
        this.systemSize = systemSize;
    }

    @Override
    public boolean transmitInRound(int round, int id, int queue) {
        if (round != lastRound) {
            currentRoundTarget = nextRoundTarget;
            nextRoundQueueMax = 0;
            lastRound = round;
        }
        if (queue > nextRoundQueueMax) {
            nextRoundQueueMax = queue;
            nextRoundTarget = id;
        }
        return queue > 0 && currentRoundTarget == id;
    }

    @Override
    public boolean awakeInRound(int round, int id) {
        return currentRoundTarget == id;
    }
}
