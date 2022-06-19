package org.ehr.algorithm;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InterleavedSelectors implements IAlgorithm {
    private List<List<Set<Integer>>> interleavedSelectors;
    private final int[] activeSubsetIndex;
    private final AtomicInteger lastSetRound = new AtomicInteger(-1);

    public InterleavedSelectors(int systemSize, int k) throws Exception {
        loadEmbeddedSelectors(systemSize, k);
        activeSubsetIndex = new int[interleavedSelectors.size()];
        Arrays.fill(activeSubsetIndex, 0);
    }

    @Override
    public boolean transmitInRound(final int round, int id, int queue) {
        int activeSelectorIndex = round % interleavedSelectors.size();
        List<Set<Integer>> activeSelector = interleavedSelectors.get(activeSelectorIndex);
        int subsetIndex = getSubsetIndex(round, activeSelectorIndex, activeSelector);
        return queue > 0 && activeSelector
                .get(subsetIndex)
                .contains(id);
    }

    @Override
    public boolean awakeInRound(final int round, int id) {
        int activeSelectorIndex = round % interleavedSelectors.size();
        return interleavedSelectors
                .get(activeSelectorIndex)
                .get(activeSubsetIndex[activeSelectorIndex])
                .contains(id);
    }

    private int getSubsetIndex(final int round, int activeSelectorIndex, List<Set<Integer>> activeSelector) {
        if (lastSetRound.get() != round) {
            lastSetRound.set(round);
            activeSubsetIndex[activeSelectorIndex] = (activeSubsetIndex[activeSelectorIndex] + 1) % activeSelector.size();
        }
        return activeSubsetIndex[activeSelectorIndex];
    }

    private void loadEmbeddedSelectors(int nInput, int k) throws URISyntaxException, IOException {
        int lgN = (int) Math.ceil(Math.log(nInput) / Math.log(2.0));
        int n = (int) Math.pow(2, lgN);

        String inputName = n + "_family.txt";
        File selectorFamilyFile = new File(Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource(inputName)).toURI());
        if (!selectorFamilyFile.exists()) {
            throw new IOException("Cannot find associated selector file in the resources folder: " + inputName);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(selectorFamilyFile))) {
            interleavedSelectors = reader.lines()
                    .map(line -> line.split(";"))
                    .map(setString -> readSetFromString(setString, k))
                    .filter(selector -> !selector.isEmpty()) // empty if size > k
                    .collect(Collectors.toList());
        }
    }

    private List<Set<Integer>> readSetFromString(String[] comaSeparatedSubsets, int k) {
        List<Set<Integer>> selector = new ArrayList<>(comaSeparatedSubsets.length);

        for (String subsetString : comaSeparatedSubsets) {
            String[] values = subsetString.split(",");
            Set<Integer> subset = Arrays.stream(values).map(Integer::parseInt).collect(Collectors.toSet());
            if (subset.size() <= k && subset.size() > 0)
                selector.add(subset);
        }
        return selector;
    }
}
