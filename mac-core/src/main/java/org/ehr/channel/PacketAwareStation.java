package org.ehr.channel;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class PacketAwareStation implements IStation {
    private final Queue<Packet> queue = new LinkedList<>();
    private final int id;
    private final AtomicInteger uniquePacketCount = new AtomicInteger(0);

    public PacketAwareStation(int id) {
        this.id = id;
    }

    @Override
    public void injectPacket(int packets, int round) {
        uniquePacketCount.addAndGet(packets);
        IntStream.range(0, packets)
                .forEach((index) -> queue.add(new Packet(round)));
    }

    @Override
    public void transmitSuccess() {
        queue.poll();
    }

    @Override
    public int getQueueSize() {
        return queue.size();
    }

    @Override
    public void setQueue(int packets) {
        IntStream.range(0, packets)
                .forEach((index) -> queue.add(new Packet(0)));
    }

    @Override
    public int getId() {
        return id;
    }

    public Queue<Packet> getQueue() {
        return queue;
    }

    public int getUniquePacketCount() {
        return uniquePacketCount.get();
    }
}
