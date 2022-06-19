package org.ehr.algorithm;

import java.util.LinkedList;

public class MoveBigToFront implements IAlgorithm {
    private final LinkedList<Integer> orderList;
    private final int systemSize;
    private int stationWithTokenIndex;
    private int nextTransmitRound;

    public MoveBigToFront(int systemSize) {
        this.systemSize = systemSize;
        this.orderList = new LinkedList<>();

        for(int i = 0; i < systemSize; i++)
            orderList.add(i);

        stationWithTokenIndex = 0;
        nextTransmitRound = 0;
    }

    @Override
    public boolean transmitInRound(int round, int id, int queue) {
        // only for stations with a token
        if (orderList.get(stationWithTokenIndex) != id)
            return false;

        // if we are a station the token was passed to this round, skip
        if(round != nextTransmitRound) {
            return false;
        }

        //station is with token, so we shall transmit
        boolean transmissionHappened = queue > 0;
        nextTransmitRound++;

        if(queue < systemSize) {
            stationWithTokenIndex = (1 + stationWithTokenIndex) % systemSize;
        } else {
            orderList.removeFirstOccurrence(id);
            orderList.addFirst(id);
            stationWithTokenIndex = 0;
        }

        return transmissionHappened;
    }

    @Override
    public boolean awakeInRound(int round, int id) {
        return true;
    }
}
