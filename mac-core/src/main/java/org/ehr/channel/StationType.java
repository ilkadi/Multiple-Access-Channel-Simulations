package org.ehr.channel;

import java.util.function.Function;

public enum StationType {
    SIMPLE(SimpleStation::new),
    QUEUE_AWARE(PacketAwareStation::new);

    private final Function<Integer, IStation> stationCreator;

    StationType(Function<Integer, IStation> stationCreator) {
        this.stationCreator = stationCreator;
    }

    public IStation getInstance(int id) {
        return stationCreator.apply(id);
    }
}
