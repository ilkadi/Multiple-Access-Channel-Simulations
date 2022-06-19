package org.ehr.adversary;

import org.ehr.channel.IStation;

import java.util.List;
import java.util.Map;

public class SrrAdversary implements IAdversary {
    private final int beta = 8;
    private final Map<Integer, Integer> strategyMap32 = Map.of(
            0, 1,
            1, 1,
            16, 1,
            17, 1,
            31, 4);
    private final Map<Integer, Integer> strategyMap64 = Map.of(
            0, 1,
            1, 1,
            32, 1,
            33, 1,
            63, 4);
    private final double rho;
    private double accumulatedTransmitPower;
    private final Map<Integer, Integer> strategyMap;
    private boolean burstReady = false;

    public SrrAdversary(double rho, int systemSize) {
        this.rho = rho;

        if(systemSize == 32)
            strategyMap = strategyMap32;
        else {
            System.out.println("Using the map for SystemSize = 64");
            strategyMap = strategyMap64;
        }

        accumulatedTransmitPower = 0.0;
    }

    @Override
    public void prepareForRound(List<IStation> stations) {
        accumulatedTransmitPower += rho;
        burstReady = accumulatedTransmitPower >= beta;
        accumulatedTransmitPower = burstReady ? accumulatedTransmitPower - beta : accumulatedTransmitPower;
    }

    @Override
    public int getInjectedPacketsByStation(int round, int stationId) {
        return burstReady && strategyMap.containsKey(stationId) ? strategyMap.get(stationId) : 0;
    }
}
