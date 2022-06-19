package org.ehr.channel;

public interface IStation {
    void injectPacket(int packets, int round);
    void transmitSuccess();
    int getQueueSize();
    void setQueue(int queue);
    int getId();
}
