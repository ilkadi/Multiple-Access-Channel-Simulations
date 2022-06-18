package org.ehr.adversary;

import org.ehr.channel.IStation;

import java.util.List;

public interface IAdversary {
    void prepareForRound(List<IStation> station);
    int getInjectedPacketsByStation(int round, int stationId);
    default void processCollision() {

    }
    default void processSilentRound() {

    }
}
