package org.ehr.adversary;

import java.util.function.BiFunction;

public enum Adversary {
    MBTF_AVERAGE(MbtfAdversary::new),
    RRW_AVERAGE(RrwAdversary::new),
    SRR_AVERAGE(SrrAdversary::new),
    STOCHASTIC_SIMPLE(stochasticWithPAndBeta(1.0, 1)),
    STOCHASTIC_P05_B1(stochasticWithPAndBeta(0.5, 1)),
    STOCHASTIC_P05_B256(stochasticWithPAndBeta(0.5, 256)),
    TWO_FAVORITES_P05_B256(twoFavoritesWithPAndBeta(0.5, 256));

    private final BiFunction<Double, Integer, IAdversary> adversaryBySystemSizeAndRho;

    Adversary(BiFunction<Double, Integer, IAdversary>  adversaryBySystemSizeAndRho) {
        this.adversaryBySystemSizeAndRho = adversaryBySystemSizeAndRho;
    }

    public IAdversary getInstance(double rho, int systemSize) {
        return adversaryBySystemSizeAndRho.apply(rho, systemSize);
    }

    private static BiFunction<Double, Integer, IAdversary> stochasticWithPAndBeta(double pGate, int beta) {
        return (rho, systemSize) -> new StochasticAdversary(rho, systemSize, pGate, beta);
    }

    private static BiFunction<Double, Integer, IAdversary> twoFavoritesWithPAndBeta(double pGate, int beta) {
        return (rho, systemSize) -> new TwoFavoritesBurstAdversary(rho, systemSize, pGate, beta);
    }
}
