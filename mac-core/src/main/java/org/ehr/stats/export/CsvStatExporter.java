package org.ehr.stats.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.ehr.utils.FileHelper.mkdirsForOutput;

public class CsvStatExporter {
    private static final String OUTPUT_DIR = "output";
    private final int publishStep;
    private final String subDirectoryPath;

    public CsvStatExporter(String subDirectoryPath, int publishStep) {
        this.publishStep = publishStep;
        this.subDirectoryPath = subDirectoryPath;
    }

    public void publishSummaryExecutionResultsForRho(String outputName, IStatProvider statProvider) {
        String filePath = OUTPUT_DIR + "/" + subDirectoryPath + "/" + outputName;
        System.out.println("Saving: " + filePath);
        mkdirsForOutput(filePath);

        String header = String.join(",", statProvider.getColumns());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(header);
            writer.newLine();

            for (int i = 0; i < statProvider.getMaxLength(); i += publishStep) {
                int round = i;
                String row = statProvider.getColumns().stream()
                                .map(column -> statProvider.getStatByColumnAndRound(column, round))
                                .collect(Collectors.joining(","));
                writer.write(row);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
