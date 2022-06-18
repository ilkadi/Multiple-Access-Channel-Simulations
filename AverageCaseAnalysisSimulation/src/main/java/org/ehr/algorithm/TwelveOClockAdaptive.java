package org.ehr.algorithm;

import java.util.LinkedList;

public class TwelveOClockAdaptive implements IAlgorithm {
    private final LinkedList<Integer> orderList;
    private final int systemSize;
    private final int bigThreshold;
    private int tokenWitholdIndex;
    private AlgorithmState algorithmState = AlgorithmState.NORMAL;

    public TwelveOClockAdaptive(int systemSize) {
        this.systemSize = systemSize;
        this.bigThreshold = systemSize * 3;
        this.orderList = new LinkedList<>();

        for(int i = 0; i < systemSize; i++)
            orderList.add(i);
    }

    @Override
    public boolean transmitInRound(int round, int id, int queue) {
        switch (algorithmState) {
            case NORMAL -> {
                int tokenIndex = round % systemSize;
                int expectedToTransmit = orderList.get(tokenIndex);
                if (expectedToTransmit == id && queue > 0) {
                    if (queue > bigThreshold) {
                        algorithmState = AlgorithmState.BIG;
                        tokenWitholdIndex = tokenIndex;
                    }
                    return true;
                }
            }
            case BIG -> {
                boolean itIsBigStation = orderList.get(tokenWitholdIndex) == id;

                if (itIsBigStation) {
                    boolean twelveOClockRound = round % systemSize == 0;
                    boolean queueBelowBigThreshold = queue <= bigThreshold;

                    if (twelveOClockRound && queueBelowBigThreshold) {
                        algorithmState = AlgorithmState.LAST_BIG;
                    }
                    return true;
                }
            }
            case LAST_BIG -> {
                boolean itIsBigStation = orderList.get(tokenWitholdIndex) == id;

                if (itIsBigStation) {
                    boolean twelveOClockRound = round % systemSize == 0;

                    if (twelveOClockRound) {
                        algorithmState = AlgorithmState.NORMAL;
                        orderList.remove(tokenWitholdIndex);
                        orderList.addFirst(id);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean awakeInRound(int round, int id) {
        boolean transmitStationAwake = AlgorithmState.NORMAL.equals(algorithmState) &&
                orderList.get(round % systemSize) == id;
        boolean bigStationAwake = (AlgorithmState.BIG.equals(algorithmState) ||
                AlgorithmState.LAST_BIG.equals(algorithmState)) &&
                orderList.get(tokenWitholdIndex) == id;
        boolean listenStationAwake = orderList.get((round + 1) % systemSize) == id;
        return transmitStationAwake || bigStationAwake || listenStationAwake;
    }

    enum AlgorithmState {
        NORMAL,
        BIG,
        LAST_BIG
    }
}
