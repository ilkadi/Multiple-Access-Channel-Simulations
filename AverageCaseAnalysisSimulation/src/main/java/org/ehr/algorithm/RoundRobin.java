package org.ehr.algorithm;

public class RoundRobin implements IAlgorithm {
    private final int systemSize;

    public RoundRobin(int systemSize) {
        this.systemSize = systemSize;
    }

    @Override
    public boolean transmitInRound(int round, int id, int queue) {
        return queue > 0 && round % systemSize == id;
    }

    @Override
    public boolean awakeInRound(int round, int id) {
        return round % systemSize == id;
    }
}
