package org.ehr.channel;

public class Station {
    private final IAlgorithm algorithm;
    private int queue;
    private final int id;

    public Station(IAlgorithm algorithm, int id) {
        this.algorithm = algorithm;
        this.id = id;

        queue = 0;
    }

    public void injectPacket(int packets) {
        queue += packets;
    }

    public boolean attemptTransmitIfAllowed(int round) {
        return algorithm.transmitInRound(round, id, queue);
    }

    public void transmitSuccess() {
        queue--;
    }

    public int getQueueSize() {
        return queue;
    }
}
