package org.ehr.adversary;

import org.ehr.channel.Station;

import java.util.*;

public class MbtfAdversary implements IAdversary {
    private final Set<Integer> targetSet;
    private final Set<Integer> nonTargetList;
    private final double rho;
    private double accumulatedTransmitPower;
    private final int maxAllowedPacketsPerTargetStation;
    private final int[] roundTargets;

    public MbtfAdversary(double rho, int systemSize) {
        this.rho = rho;
        this.maxAllowedPacketsPerTargetStation = systemSize - 1;
        this.roundTargets = new int[systemSize];

        targetSet = new HashSet<>();
        nonTargetList = new HashSet<>();
        int targetSize = (int) Math.floor(rho * maxAllowedPacketsPerTargetStation);
        int distance = (int) ((double) systemSize / (double) (targetSize)) - 1;

        int currentId = 1;
        for (int i = 1; i <= targetSize; i++) {
            targetSet.add(currentId);
            currentId += distance;
        }

        for (int i = 0; i < systemSize; i++) {
            if (!targetSet.contains(i))
                nonTargetList.add(i);
        }

        accumulatedTransmitPower = 0.0;
    }

    @Override
    public int getInjectedPacketsByStation(int round, int stationId) {
        return roundTargets[stationId];
    }

    @Override
    public void prepareForRound(List<Station> stations) {
        accumulatedTransmitPower += rho;
        Arrays.fill(roundTargets, 0);

        if (accumulatedTransmitPower > 1.0) {
            int targetStationId = getTargetStationId(stations);
            roundTargets[targetStationId] = 1;
        }

    }

    private int getTargetStationId(List<Station> stations) {
        int targetStationId = 0;
        for (int targetId : targetSet) {
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
