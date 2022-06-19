package org.ehr.channel;

public class SimpleStation implements IStation {
    private int queue;
    private final int id;

    public SimpleStation(int id) {
        this.id = id;
        queue = 0;
    }

    @Override
    public void injectPacket(int packets, int round) {
        queue += packets;
    }

    @Override
    public void transmitSuccess() {
        queue--;
    }

    @Override
    public int getQueueSize() {
        return queue;
    }

    @Override
    public void setQueue(int queue) {
        this.queue = queue;
    }

    @Override
    public int getId() {
        return id;
    }
}
