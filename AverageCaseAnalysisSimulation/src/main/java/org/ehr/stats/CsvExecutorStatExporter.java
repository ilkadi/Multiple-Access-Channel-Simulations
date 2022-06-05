package org.ehr.stats;

import java.io.*;
import java.util.Arrays;

public class CsvExecutorStatExporter implements IExecutorStatExporter {
    public static final String OUTPUT_DIR = "output";

    @Override
    public void publishSummaryExecutionResultsForRho(String rhoString,
                                                     String outputName, ExecutorRhoStats executorRhoStats) {
        String filePath = OUTPUT_DIR + "/" + rhoString + "/" + outputName;
        System.out.println("Saving: " + filePath);
        mkdirsForOutput(filePath);

        String header = String.join(",", "rounds", "max-max", "max-avg", "avg-max", "avg-avg", "max-energy", "avg-energy");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(header);
            writer.newLine();
            for (int i = 0; i < executorRhoStats.getMaxMax().length; i++) {
                if (i % 1000 == 0) {
                    String row = String.join(",", Integer.toString(i),
                            String.format("%.3f", executorRhoStats.getMaxMax()[i]),
                            String.format("%.3f", executorRhoStats.getMaxAvg()[i]),
                            String.format("%.3f", executorRhoStats.getAvgMax()[i]),
                            String.format("%.3f", executorRhoStats.getAvgAvg()[i]),
                            String.format("%.3f", executorRhoStats.getMaxEnergy()[i]),
                            String.format("%.3f", executorRhoStats.getAvgEnergy()[i]));
                    writer.write(row);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publishSummaryExecutionResults(String outputName, ExecutorRhoStats executorRhoStats) {

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void mkdirsForOutput(String path) {
        String[] pathSegments = path.split("/");
        String pathWithoutLastSegment = String.join("/",
                Arrays.copyOf(pathSegments, pathSegments.length - 1));

        File outputDir = new File(pathWithoutLastSegment);
        outputDir.mkdirs();
    }
}