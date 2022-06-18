package org.ehr.algorithm;

import java.util.LinkedList;

public class TwelveOClockFS implements IAlgorithm {
    private final LinkedList<Integer> orderList;
    private final int systemSize;
    private final int bigThreshold;
    private int tokenWitholdIndex;
    private AlgorithmState algorithmState = AlgorithmState.NORMAL;

    public TwelveOClockFS(int systemSize) {
        this.systemSize = systemSize;
        this.bigThreshold = systemSize * 2;
        this.orderList = new LinkedList<>();

        for(int i = 0; i < systemSize; i++)
            orderList.add(i);
    }

    @Override
    public boolean transmitInRound(int round, int id, int queue) {
        int tokenIndex = round % systemSize;
        switch (algorithmState) {
            case NORMAL -> {
                if (orderList.get(tokenIndex) == id && queue > 0) {
                    if (queue > bigThreshold) {
                        tokenWitholdIndex = tokenIndex;
                        algorithmState = AlgorithmState.BIG;
                    }
                    return true;
                }
            }
            case BIG -> {
                boolean itIsBigStation = orderList.get(tokenWitholdIndex) == id;
                boolean itIsBigStationSuccessorRound = tokenIndex == (tokenWitholdIndex + 1) % systemSize;

                if (itIsBigStation) {
                    boolean twelveOClockRound = round % systemSize == 0;
                    boolean queueBelowBigThreshold = queue <= bigThreshold;

                    if (twelveOClockRound && queueBelowBigThreshold) {
                        algorithmState = AlgorithmState.NORMAL;
                        orderList.remove(tokenWitholdIndex);
                        orderList.addFirst(id);
                    }
                    return true;
                }

                if (itIsBigStationSuccessorRound)
                    return orderList.get(tokenIndex) == id && queue > 0;
            }
        }
        return false;
    }

    @Override
    public boolean awakeInRound(int round, int id) {
        return false;
    }

    @Override
    public void processTransmission(int transmittedId) {
    }

    @Override
    public void processSilence() {
    }

    @Override
    public void processCollision() {
    }

    enum AlgorithmState {
        NORMAL,
        BIG
    }
}

