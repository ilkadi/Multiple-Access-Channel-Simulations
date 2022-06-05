package org.ehr.algorithm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class InterleavedSelectorsTest {

    @Test
    @Disabled
    public void generateAndTestFourSelectiveFamily() throws IOException {
        generateAndTestSelectiveFamily(4);
    }

    @Test
    @Disabled
    public void generateAndTestEightSelectiveFamily() throws IOException {
        generateAndTestSelectiveFamily(8);
    }

    @Test
    @Disabled
    public void generateAndTestSixteenSelectiveFamily() throws IOException {
        generateAndTestSelectiveFamily(16);
    }

    @Test
    @Disabled
    public void generateAndTestThirtyTwoSelectiveFamily() throws IOException {
        generateAndTestSelectiveFamily(32);
    }

    public void generateAndTestSelectiveFamily(int n) throws IOException {
        InterleavedSelectorsGenerator generator = new InterleavedSelectorsGenerator();
        List<List<Set<Integer>>> selectiveFamily = generator.generateAndTestSelectiveFamily(n);

        for (List<Set<Integer>> selector : selectiveFamily) {
            System.out.println("\nselector(" + selector.size() + "):");
            for (Set<Integer> subset : selector)
                System.out.print(subset);
        }

        String outputName = n + "_family.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {
            for (List<Set<Integer>> selector : selectiveFamily) {
                for (Set<Integer> subset : selector) {
                    for (Integer element : subset)
                        writer.write(element + ",");
                    writer.write(";");
                }
                writer.newLine();
            }
        }
    }
}
