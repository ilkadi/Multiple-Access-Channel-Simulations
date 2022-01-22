package org.ehr.adversary;

import org.ehr.channel.IAdversary;
import org.ehr.channel.Station;

import java.util.List;
import java.util.Map;

public class SrrAdversary implements IAdversary {
    private static final int beta = 8;
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

    public SrrAdversary(double rho, int systemSize) {
        this.rho = rho;

        if(systemSize == 32)
            strategyMap = strategyMap32;
        else
            strategyMap = strategyMap64;

        accumulatedTransmitPower = 0.0;
    }

    @Override
    public void processCollision() {
    }

    @Override
    public void processSilentRound() {
    }

    @Override
    public void tickRound() {
        accumulatedTransmitPower += rho;
    }

    @Override
    public Map<Integer, Integer> getTargetStationIds(int round, List<Station> stations) {
        return strategyMap;
    }

    @Override
    public int injectedPackets() {
        if (accumulatedTransmitPower >= beta) {
            accumulatedTransmitPower -= beta;
            return beta;
        }

        return 0;
    }
}
