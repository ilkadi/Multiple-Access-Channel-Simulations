package org.ehr.algorithm;

import com.google.common.collect.Streams;
import org.apache.commons.math3.util.Combinations;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class InterleavedSelectorsGenerator {
    public List<List<Set<Integer>>> generateAndTestSelectiveFamily(int nInput) {
        int lgN = (int) Math.ceil(Math.log(nInput) / Math.log(2.0));
        int n = (int) Math.pow(2, lgN);

        System.out.printf("Initialising generation of nInput=%s [n=%s, lgN=%s] selective family\n", nInput, n, lgN);
        List<List<Set<Integer>>> selectiveFamily = new ArrayList<>(lgN);

        for (int i = 1; i <= lgN; i++) {
            int omega = (int) Math.pow(2, i);
            int m = omega * lgN;

            System.out.printf("Generating (n=%s, omega=%s) selector of length m=%s...\n", n, omega, m);
            selectiveFamily.add(generateSelector(m, omega, n));
        }
        return selectiveFamily;
    }

    private List<Set<Integer>> generateSelector(int m, int omega, int n) {
        int elementsHit = Math.max(omega / 4, 1);

        int attemptLimit = 1000;
        int currentAttempt = 0;

        while (currentAttempt < attemptLimit) {
            System.gc();
            currentAttempt++;
            System.out.printf("[n=%s][omega=%s] generate selector attempt: %s\n", n, omega, currentAttempt);

            List<int[]> selector = generateRandomSelector(n, n / omega, m);
            boolean selectionExists = selectionExists(selector, n, omega, elementsHit);
            if (selectionExists)
                return selector.stream().map(this::convertSet).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<int[]> generateRandomSelector(int n, int subsetElements, int m) {
        long allCombinations = CombinatoricsUtils.binomialCoefficient(n, subsetElements);
        Iterator<int[]> combinationsIterator = new Combinations(n, subsetElements).iterator();
        if (allCombinations <= m)
            return Streams.stream(combinationsIterator)
                    .collect(Collectors.toList());

        Random r = new Random();
        List<int[]> selector = new ArrayList<>(m);
        Set<Integer> randomSubsetIndexes = new HashSet<>(m);
        while (randomSubsetIndexes.size() < m)
            randomSubsetIndexes.add(r.nextInt((int) allCombinations));
        int maxIndex = Collections.max(randomSubsetIndexes);

        for (int i = 0; i <= maxIndex; i++) {
            int[] subset = combinationsIterator.next();

            if (randomSubsetIndexes.contains(i))
                selector.add(subset);
        }
        return selector;
    }

    private boolean selectionExists(List<int[]> selector, int n, int omega, int elementsHit) {
        for (int elements = omega / 2; elements <= omega; elements++) {
            System.out.print("\tTesting against " + elements + "-sized subsets... ");

            Iterator<int[]> allSubsetsOfSize = new Combinations(n, elements).iterator();
            boolean selectionExists = Streams.stream(allSubsetsOfSize)
                    .allMatch(x -> selectionExists(selector, x, elementsHit));

            if (!selectionExists) {
                System.out.println("Fail.");
                return false;
            } else {
                System.out.println("Pass.");
            }
        }
        return true;
    }

    private boolean selectionExists(List<int[]> selector, int[] x, int selections) {
        int currentCount = 0;
        long xLong = intArrayToLongBits(x);

        for (int[] subset : selector) {
            long subsetLong = intArrayToLongBits(subset);
            long intersection = xLong & subsetLong;
            if (Long.bitCount(intersection) == 1)
                currentCount++;
        }
        return currentCount >= selections;
    }

    private long intArrayToLongBits(int[] elements) {
        long set = 0L;
        for (int element : elements)
            set |= 1L << element;
        return set;
    }

    private Set<Integer> convertSet(int[] elements) {
        return Arrays.stream(elements).boxed().collect(Collectors.toSet());
    }

    private Set<Integer> convertLongToSet(long subsetLong, int[] set) {
        Set<Integer> subset = new HashSet<>(set.length);
        for (int j = 0; j < set.length; j++)
            if ((subsetLong & (1L << j)) > 0)
                subset.add(set[j]);
        return subset;
    }

    private List<Long> allSubsetsAsLong(long n) {
        List<Long> subsets = new ArrayList<>();
        for (int i = 0; i < (1L << n); i++) {
            long subset = 0L;
            for (int j = 0; j < n; j++)
                if ((i & (1L << j)) > 0)
                    subset |= 1L << j;

            subsets.add(subset);
        }
        return subsets;
    }

    // works till n=16
    private List<Set<Integer>> allSubsets(int n, int[] set) {
        List<Set<Integer>> subsets = new ArrayList<>();
        for (int i = 0; i < (1L << n); i++) {
            Set<Integer> subset = new HashSet<>(n);
            for (int j = 0; j < n; j++)
                if ((i & (1L << j)) > 0)
                    subset.add(set[j]);

            subsets.add(subset);
        }
        return subsets;
    }
}
