package org.ehr.channel;

public class Station {
    private int queue;
    private final int id;

    public Station(int id) {
        this.id = id;
        queue = 0;
    }

    public void injectPacket(int packets) {
        queue += packets;
    }

    public void transmitSuccess() {
        queue--;
    }

    public int getQueueSize() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public int getId() {
        return id;
    }
}
