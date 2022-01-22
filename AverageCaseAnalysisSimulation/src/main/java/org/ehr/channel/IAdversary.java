package org.ehr.channel;

import java.util.List;
import java.util.Map;

public interface IAdversary {
    void tickRound();
    Map<Integer, Integer> getTargetStationIds(int round, List<Station> stations);
    int injectedPackets();
    void processCollision();
    void processSilentRound();
}
