package org.ehr.adversary;

import org.ehr.channel.IAdversary;
import org.ehr.channel.Station;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class StochasticAdversary implements IAdversary {
    private final double rho;
    private final int systemSize;
    private static final int beta = 1;
    private double accumulatedTransmitPower;
    private final Random r;

    public StochasticAdversary(double rho, int systemSize) {
        this.rho = rho;
        this.systemSize = systemSize;

        accumulatedTransmitPower = 0.0;
        r = new Random();
    }

    @Override
    public void tickRound() {
        accumulatedTransmitPower += rho;
    }

    @Override
    public Map<Integer, Integer> getTargetStationIds(int round, List<Station> stations) {
        return Map.of(r.nextInt(systemSize), 1);
    }

    @Override
    public int injectedPackets() {
        if (accumulatedTransmitPower >= beta) {
            accumulatedTransmitPower -= beta;
            return beta;
        }

        return 0;
    }

    @Override
    public void processCollision() {

    }

    @Override
    public void processSilentRound() {

    }
}
