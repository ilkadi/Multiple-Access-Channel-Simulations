package org.ehr.algorithm;

import javax.swing.plaf.nimbus.State;
import java.util.function.Function;

import static org.ehr.algorithm.Backoff.WINDOW_SIZE_LIMIT;

public enum Algorithm {
    MBTF(MoveBigToFront::new),
    RRW(RoundRobinWithholding::new),
    SRR(SearchRoundRobin::new),
    ROUND_ROBIN(RoundRobin::new),
    STATE_AWARE(StateAware::new),
    TWELVE_OC_ADAPTIVE(TwelveOClockAdaptive::new),
    TWELVE_OC_FS(TwelveOClockFS::new),
    EIGHT_LIGHT_IS(getInterleavedSelectors(8)),
    SIXTEEN_LIGHT_IS(getInterleavedSelectors(16)),
    BACKOFF_POL_LINEAR(getBackoffFunction((w) -> Math.min(2 * w, WINDOW_SIZE_LIMIT))),
    BACKOFF_POL_SQUARE(getBackoffFunction((w) -> Math.min(2 * w * w, WINDOW_SIZE_LIMIT))),
    BACKOFF_EXPONENTIAL(getBackoffFunction((w) -> Math.min((int) Math.round(Math.pow(2, w)), WINDOW_SIZE_LIMIT)));

    private final Function<Integer, IAlgorithm> algorithmBySystemSize;

    Algorithm(Function<Integer,IAlgorithm> algorithmBySystemSize) {
        this.algorithmBySystemSize = algorithmBySystemSize;
    }

    public IAlgorithm getInstance(int systemSize) {
        return algorithmBySystemSize.apply(systemSize);
    }

    private static Function<Integer, IAlgorithm> getBackoffFunction(Function<Integer, Integer> backoffFunction) {
        return (systemSize) -> new Backoff(systemSize, backoffFunction);
    }

    private static Function<Integer, IAlgorithm> getInterleavedSelectors(int k) {
        return (systemSize) -> {
            try {
                return new InterleavedSelectors(systemSize, k);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
