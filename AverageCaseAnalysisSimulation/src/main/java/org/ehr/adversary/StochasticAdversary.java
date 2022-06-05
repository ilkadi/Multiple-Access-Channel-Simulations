package org.ehr.adversary;

import org.ehr.channel.Station;

import java.util.*;

public class StochasticAdversary implements IAdversary {
    private final double pGate;
    private final double rho;
    private final int systemSize;
    private final int beta;
    private double accumulatedTransmitPower;
    private final Random r;
    private final int[] roundTargets;

    public StochasticAdversary(double rho, int systemSize, double pGate, int beta) {
        this.pGate = pGate;
        this.beta = beta;
        this.rho = rho;
        this.systemSize = systemSize;
        this.roundTargets = new int[systemSize];

        accumulatedTransmitPower = 0.0;
        r = new Random();
    }

    @Override
    public void prepareForRound(List<Station> stations) {
        Arrays.fill(roundTargets, 0);
        accumulatedTransmitPower += rho;

        if (injectThisRound()) {
            int burstSize = (int) Math.floor(accumulatedTransmitPower);
            accumulatedTransmitPower -= Math.floor(burstSize);
            for (int i = 0; i < burstSize; i++)
                roundTargets[r.nextInt(systemSize)] += 1;
        }

    }

    @Override
    public int getInjectedPacketsByStation(int round, int stationId) {
        return roundTargets[stationId];
    }

    private boolean injectThisRound() {
        return accumulatedTransmitPower >= beta || r.nextDouble() <= pGate;
    }
}
