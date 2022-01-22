package org.ehr.channel;

public interface IAlgorithm {
    boolean transmitInRound(int round, int id, int queue);
    void reset();

    default void processTransmission() {

    }

    default void processSilence() {

    }

    default void processCollision() {

    }
}
