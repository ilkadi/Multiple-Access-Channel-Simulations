package org.ehr.channel;

import java.util.List;

public class StatRecorder {
    private final List<Station> stations;
    private double cumulativePresenceInQueues;
    private int messageCount;
    private int round;

    public StatRecorder(List<Station> stations) {
        this.stations = stations;
        reset();
    }

    public void reset() {
        cumulativePresenceInQueues = 0.0;
        messageCount = 0;
    }

    public void increaseMessageCount(int packets) {
        messageCount += packets;
    }

    public void processRoundStats(int round) {
        double currentRoundPacketPresence = stations.stream()
                .map(Station::getQueueSize)
                .mapToInt(Integer::intValue)
                .sum();

        cumulativePresenceInQueues += currentRoundPacketPresence;
        this.round = round;
    }

    public void printLastResults() {
        System.out.println(cumulativePresenceInQueues / (double)(round) + "\t\t" +
                cumulativePresenceInQueues / (double)(messageCount));
    }

    public double getAverageLatency() {
        return cumulativePresenceInQueues / (double)(messageCount);
    }

    public double getAverageQueue() {
        return cumulativePresenceInQueues / (double)(round);
    }
}
