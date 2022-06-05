package org.ehr.adversary;

import org.ehr.channel.Station;

import java.util.List;

public interface IAdversary {
    void prepareForRound(List<Station> station);
    int getInjectedPacketsByStation(int round, int stationId);
    default void processCollision() {

    }
    default void processSilentRound() {

    }
}
