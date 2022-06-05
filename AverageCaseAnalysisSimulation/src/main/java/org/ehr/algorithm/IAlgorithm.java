package org.ehr.algorithm;

public interface IAlgorithm {
    boolean transmitInRound(int round, int id, int queue);
    boolean awakeInRound(int round, int id);

    default void processTransmission(int transmittedId) {

    }

    default void processSilence() {

    }

    default void processCollision() {
    }
}
