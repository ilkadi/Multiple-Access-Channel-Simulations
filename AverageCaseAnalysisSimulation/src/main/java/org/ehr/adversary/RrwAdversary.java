package org.ehr.adversary;

import org.ehr.channel.IAdversary;
import org.ehr.channel.Station;

import java.util.List;
import java.util.Map;

public class RrwAdversary implements IAdversary {
    private final double rho;
    private double accumulatedTransmitPower;
    private final int systemSize;
    private int targetStationId;

    public RrwAdversary(double rho, int systemSize) {
        this.rho = rho;
        this.systemSize = systemSize;

        accumulatedTransmitPower = 0.0;
        targetStationId = systemSize - 1;
    }

    @Override
    public void processCollision() {

    }

    @Override
    public void processSilentRound() {
        targetStationId = (systemSize + targetStationId - 1) % systemSize;
    }

    @Override
    public void tickRound() {
        accumulatedTransmitPower += rho;
    }

    @Override
    public Map<Integer, Integer> getTargetStationIds(int round, List<Station> stations) {
        return Map.of(targetStationId, 1);
    }

    @Override
    public int injectedPackets() {
        if(accumulatedTransmitPower >= 1.0) {
            accumulatedTransmitPower -= 1.0;
            return 1;
        }

        return 0;
    }
}
