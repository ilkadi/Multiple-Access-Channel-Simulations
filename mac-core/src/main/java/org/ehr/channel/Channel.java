package org.ehr.channel;

import org.ehr.adversary.IAdversary;
import org.ehr.algorithm.IAlgorithm;
import org.ehr.stats.ISimulationsStatRecorder;

import java.util.Arrays;
import java.util.List;

public class Channel {
    private final List<IStation> stations;
    private final IAlgorithm algorithm;
    private final IAdversary adversary;
    private final ISimulationsStatRecorder statRecorder;

    private final boolean[] roundAwakeStations;
    private final boolean[] roundTransmittingStations;
    private int lastTransmittedStation;
    private int transmissionsCount;

    Channel(List<IStation> stations, IAlgorithm algorithm,
            IAdversary adversary, ISimulationsStatRecorder statRecorder) {
        this.stations = stations;
        this.adversary = adversary;
        this.statRecorder = statRecorder;
        this.algorithm = algorithm;

        this.roundTransmittingStations = new boolean[stations.size()];
        this.roundAwakeStations = new boolean[stations.size()];
    }

    public void tickRound(final int round) {
        resetRoundCounters();

        processInjectionStage(round);
        processTransmissionStage(round);
        processListeningStage(round);

        statRecorder.recordAwakeStations(round, roundAwakeStations);
        statRecorder.recordStationsState(round, stations);
    }

    private void processInjectionStage(final int round) {
        adversary.prepareForRound(stations);

        for (IStation station : stations) {
            int packets = adversary.getInjectedPacketsByStation(round, station.getId());
            station.injectPacket(packets, round);
        }
    }

    private void processTransmissionStage(final int round) {
        for (int id = 0; id < stations.size(); id++) {
            roundTransmittingStations[id] = algorithm.transmitInRound(round, id, stations.get(id).getQueueSize());
            if (roundTransmittingStations[id]) {
                lastTransmittedStation = id;
                transmissionsCount++;
            }
        }
    }

    private void processListeningStage(final int round) {
        for (int id = 0; id < stations.size(); id++)
            roundAwakeStations[id] = algorithm.awakeInRound(round, stations.get(id).getId());

        if (transmissionsCount == 1) {
            stations.get(lastTransmittedStation).transmitSuccess();
            algorithm.processTransmission(lastTransmittedStation);
            //System.out.println(round + ": " + lastTransmittedStation + "->");
        } else if (transmissionsCount > 1) {
            adversary.processCollision();
            algorithm.processCollision();
            //System.out.println(round + ": !");
        } else {
            adversary.processSilentRound();
            algorithm.processSilence();
            //System.out.println(round + ": -");
        }
    }

    private void resetRoundCounters() {
        Arrays.fill(roundTransmittingStations, false);
        Arrays.fill(roundAwakeStations, false);
        lastTransmittedStation = 0;
        transmissionsCount = 0;
    }
}
