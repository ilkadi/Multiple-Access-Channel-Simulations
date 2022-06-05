package org.ehr.adversary;

import org.ehr.channel.Station;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TwoFavoritesBurstAdversary implements IAdversary {
    private final int beta;
    private final double pGate;
    private final double rho;
    private final int systemSize;
    private double accumulatedTransmitPower;
    private final Random r;
    private final int[] roundTargets;

    public TwoFavoritesBurstAdversary(double rho, int systemSize, double pGate, int beta) {
        this.pGate = pGate;
        this.beta = beta;
        this.rho = rho;
        this.systemSize = systemSize;
        this.roundTargets = new int[systemSize];

        accumulatedTransmitPower = 0.0;
        r = new Random();

        if (systemSize < 3)
            throw new RuntimeException("TwoFavoritesAdversary requires system size of at least 3 to operate!");
    }

    @Override
    public void prepareForRound(List<Station> stations) {
        Arrays.fill(roundTargets, 0);
        accumulatedTransmitPower += rho;

        if (injectThisRound()) {
            int burstSize = (int) Math.floor(accumulatedTransmitPower);
            accumulatedTransmitPower -= Math.floor(burstSize);
            for (int i = 0; i < burstSize; i++) {
                int pickFromThree = r.nextInt(3);

                if (pickFromThree == 2) {
                    roundTargets[2 + r.nextInt(systemSize - 2)] += 1;
                } else {
                    roundTargets[pickFromThree] += 1;
                }
            }
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
