package org.ehr.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Backoff implements IAlgorithm {
    public static final int WINDOW_SIZE_LIMIT = 2048;
    private final int[] windowByStation;
    private final int[] countdownByStation;
    private final List<Integer> roundTransmissionIds;
    private final Function<Integer, Integer> backoffFunction;
    private final Random r;

    public Backoff(int systemSize, Function<Integer, Integer> backoffFunction) {
        this.windowByStation = new int[systemSize];
        this.countdownByStation = new int[systemSize];
        this.roundTransmissionIds = new ArrayList<>(systemSize);

        this.backoffFunction = backoffFunction;
        this.r = new Random();

        Arrays.fill(windowByStation, 0);
        Arrays.fill(countdownByStation, 0);
    }

    @Override
    public void processTransmission(int transmittedId) {
        windowByStation[transmittedId] = 0;
        roundTransmissionIds.clear();
    }

    @Override
    public void processCollision() {
        for (Integer stationId : roundTransmissionIds) {
            if (backoffFunction.apply(windowByStation[stationId]) < WINDOW_SIZE_LIMIT)
                windowByStation[stationId] += 1;
            countdownByStation[stationId] = r.nextInt(backoffFunction.apply(windowByStation[stationId]));
        }
        roundTransmissionIds.clear();
    }

    @Override
    public boolean transmitInRound(int round, int id, int queue) {
        countdownByStation[id] = countdownByStation[id] > 0 ? --countdownByStation[id] : 0;
        boolean attemptTransmission = queue > 0 && countdownByStation[id] == 0;
        if (attemptTransmission)
            roundTransmissionIds.add(id);
        return attemptTransmission;
    }

    @Override
    public boolean awakeInRound(int round, int id) {
        return countdownByStation[id] == 0;
    }
}
