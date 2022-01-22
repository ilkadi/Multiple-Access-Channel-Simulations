package org.ehr.algorithm;

import org.ehr.channel.IAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SearchRoundRobin implements IAlgorithm {
    private final List<Integer> allStations;
    private final Stack<List<Integer>> executionStack;
    private List<Integer> searchGroup;
    private int nextTransmitRound;
    private boolean channelListeningProcessed = false;

    public SearchRoundRobin(int systemSize) {
        allStations = new ArrayList<>(systemSize);
        for(int i = 0; i < systemSize; i++)
            allStations.add(i);

        executionStack = new Stack<>();
        searchGroup  = new ArrayList<>();
        nextTransmitRound = 0;
    }

    @Override
    public void reset() {
        executionStack.clear();
        executionStack.push(allStations);
        nextTransmitRound = 0;
    }

    @Override
    public boolean transmitInRound(int round, int id, int queue) {
        if (round == nextTransmitRound) {
            searchGroup = executionStack.pop();
            nextTransmitRound++;
            channelListeningProcessed = false;
        }
        return searchGroup.contains(id) && queue > 0;
    }

    @Override
    public void processTransmission() {
        if(channelListeningProcessed)
            return;

        executionStack.push(searchGroup);
        channelListeningProcessed = true;
    }

    @Override
    public void processSilence() {
        if (executionStack.isEmpty()) {
            executionStack.push(allStations);
        }
    }

    @Override
    public void processCollision() {
        if(channelListeningProcessed)
            return;

        int groupSearchDivisionPoint = searchGroup.size() / 2;

        executionStack.push(searchGroup.subList(0, groupSearchDivisionPoint));
        executionStack.push(searchGroup.subList(groupSearchDivisionPoint, searchGroup.size()));
        channelListeningProcessed = true;
    }
}
