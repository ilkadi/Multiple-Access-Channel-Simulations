package org.ehr.adversary;

import org.ehr.channel.IStation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MbtfAdversary implements IAdversary {
    private final List<Integer> targetList;
    private final List<Integer> nonTargetList;
    private final double rho;
    private double accumulatedTransmitPower;
    private final int maxAllowedPacketsPerTargetStation;
    private final int[] roundTargets;

    public MbtfAdversary(double rho, int systemSize) {
        this.rho = rho;
        this.maxAllowedPacketsPerTargetStation = systemSize - 1;
        this.roundTargets = new int[systemSize];

        targetList = new ArrayList<>();
        nonTargetList = new ArrayList<>();
        int targetSize = (int) Math.floor(rho * maxAllowedPacketsPerTargetStation);
        int distance = (int) ((double) systemSize / (double) (targetSize)) - 1;

        int currentId = 1;
        for (int i = 1; i <= targetSize; i++) {
            targetList.add(currentId);
            currentId += distance;
        }

        for (int i = 0; i < systemSize; i++) {
            if (!targetList.contains(i))
                nonTargetList.add(i);
        }

        accumulatedTransmitPower = 0.0;
    }

    @Override
    public int getInjectedPacketsByStation(int round, int stationId) {
        return roundTargets[stationId];
    }

    @Override
    public void prepareForRound(List<IStation> stations) {
        accumulatedTransmitPower += rho;
        Arrays.fill(roundTargets, 0);

        if (accumulatedTransmitPower > 1.0) {
            int targetStationId = getTargetStationId(stations);
            roundTargets[targetStationId] = 1;
            accumulatedTransmitPower -= 1.0;
        }

    }

    private int getTargetStationId(List<IStation> stations) {
        int targetStationId = 0;
        for (int targetId : targetList) {
            if (stations.get(targetId).getQueueSize() < maxAllowedPacketsPerTargetStation) {
                return targetId;
            }
        }

        for (int nonTargetId : nonTargetList) {
            if (stations.get(nonTargetId).getQueueSize() < maxAllowedPacketsPerTargetStation) {
                return nonTargetId;
            }
        }
        return  targetStationId;
    }
}
