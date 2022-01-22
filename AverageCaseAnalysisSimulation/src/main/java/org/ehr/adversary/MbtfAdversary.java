package org.ehr.adversary;

import org.ehr.channel.IAdversary;
import org.ehr.channel.Station;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MbtfAdversary implements IAdversary {
    private final LinkedList<Integer> targetList;
    private final LinkedList<Integer> nonTargetList;
    private final double rho;
    private double accumulatedTransmitPower;
    private final int maxAllowedPacketsPerTargetStation;

    public MbtfAdversary(double rho, int systemSize) {
        this.rho = rho;
        this.maxAllowedPacketsPerTargetStation = systemSize - 1;

        targetList = new LinkedList<>();
        nonTargetList = new LinkedList<>();
        int targetSize  = (int) Math.floor(rho * maxAllowedPacketsPerTargetStation);
        int distance = (int) ((double) systemSize / (double) (targetSize)) - 1;

        int currentId = 1;
        for(int i = 1; i <= targetSize; i++) {
            targetList.add(currentId);
            currentId += distance;
        }

        for(int i = 0; i < systemSize; i++) {
            if(!targetList.contains(i))
                nonTargetList.add(i);
        }

        accumulatedTransmitPower = 0.0;
    }

    @Override
    public Map<Integer, Integer> getTargetStationIds(int round, List<Station> stations) {
        int targetStationId = 0;
        for(int targetId : targetList) {
            if(stations.get(targetId).getQueueSize() < maxAllowedPacketsPerTargetStation) {
                return Map.of(targetId, 1);
            }
        }

        for(int nonTargetId : nonTargetList) {
            if(stations.get(nonTargetId).getQueueSize() < maxAllowedPacketsPerTargetStation) {
                return Map.of(nonTargetId, 1);
            }
        }

        return Map.of(targetStationId, 1);
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
    public int injectedPackets() {
        if (accumulatedTransmitPower >= 1.0) {
            accumulatedTransmitPower -= 1.0;
            return 1;
        }

        return 0;
    }
}
