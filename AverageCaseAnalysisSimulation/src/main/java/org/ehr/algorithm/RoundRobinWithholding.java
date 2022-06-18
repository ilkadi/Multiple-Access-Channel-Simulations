package org.ehr.algorithm;

public class RoundRobinWithholding implements IAlgorithm {
    private final int systemSize;
    private int stationWithTokenId;
    private int nextTransmitRound;

    public RoundRobinWithholding(int systemSize) {
        this.systemSize = systemSize;
        stationWithTokenId = 0;
        nextTransmitRound = 0;
    }

    @Override
    public boolean transmitInRound(int round, int id, int queue) {
        if (stationWithTokenId != id)
            return false;

        // only for stations with token

        if (queue == 0) {
            stationWithTokenId = (1 + stationWithTokenId) % systemSize;
            nextTransmitRound = round + 1;
            return false;
        }

        // queue > 0

        if (nextTransmitRound == round) {
            nextTransmitRound++;
            return true;
        }

        // station with token in the silent round
        return false;
    }

    @Override
    public boolean awakeInRound(int round, int id) {
        return true;
    }
}
