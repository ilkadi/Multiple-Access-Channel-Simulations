package org.ehr.channel;

import java.util.List;
import java.util.stream.Collectors;

public class Channel {
    private final List<Station> stations;
    private final IAlgorithm algorithm;
    private final IAdversary adversary;
    private final StatRecorder statRecorder;

    public Channel(List<Station> stations, IAlgorithm algorithm, IAdversary adversary, StatRecorder statRecorder) {
        this.stations = stations;
        this.adversary = adversary;
        this.statRecorder = statRecorder;
        this.algorithm = algorithm;
    }

    public void executeRound(final int round) {
        int adversaryPackets = adversary.injectedPackets();

        if (adversaryPackets > 0) {
            statRecorder.increaseMessageCount(adversaryPackets);

            adversary.getTargetStationIds(round, stations)
                    .forEach((id, packets) -> stations.get(id)
                            .injectPacket(packets));
        }

        List<Station> stationsAttemptingToTransmit = stations.stream()
                .filter(s -> s.attemptTransmitIfAllowed(round))
                .collect(Collectors.toList());

        if(stationsAttemptingToTransmit.size() == 1) {
            stationsAttemptingToTransmit.get(0).transmitSuccess();
            algorithm.processTransmission();
        } else if(stationsAttemptingToTransmit.size() > 1) {
            adversary.processCollision();
            algorithm.processCollision();
        } else {
            adversary.processSilentRound();
            algorithm.processSilence();
        }
    }
}
