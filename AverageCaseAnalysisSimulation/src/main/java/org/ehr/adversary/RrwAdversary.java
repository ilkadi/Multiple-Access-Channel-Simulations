package org.ehr.adversary;

import org.ehr.channel.IStation;

import java.util.List;

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
    public void processSilentRound() {
        targetStationId = (systemSize + targetStationId - 1) % systemSize;
    }

    @Override
    public void prepareForRound(List<IStation> stations) {
        accumulatedTransmitPower += rho;
    }

    @Override
    public int getInjectedPacketsByStation(int round, int stationId) {
        if (stationId == targetStationId && accumulatedTransmitPower >= 1.0 ) {
            accumulatedTransmitPower -= 1.0;
            return 1;
        }
        return 0;
    }
}
