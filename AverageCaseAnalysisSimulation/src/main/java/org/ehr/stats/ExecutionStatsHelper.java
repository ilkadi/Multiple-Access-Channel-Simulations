package org.ehr.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.ehr.stats.CsvExecutorStatExporter.OUTPUT_DIR;
import static org.ehr.utils.FileHelper.mkdirsForOutput;

public class ExecutionStatsHelper {
    private final ArrayList<ExecutionLastStats> executionLastStats = new ArrayList<>();

    public void add(ExecutionLastStats lastStats) {
        executionLastStats.add(lastStats);
    }

    public void publish(String outputName) {
        String filePath = OUTPUT_DIR + "/" + outputName;
        System.out.println("Saving: " + filePath);
        mkdirsForOutput(filePath);

        String header = String.join(",", "rho", "max-max", "max-avg", "avg-max", "avg-avg", "max-energy", "avg-energy");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(header);
            writer.newLine();
            for (ExecutionLastStats executionLastStat : executionLastStats) {
                String row = String.join(",",
                        String.format("%.3f", executionLastStat.getRho()),
                        String.format("%.3f", executionLastStat.getMaxMax()),
                        String.format("%.3f", executionLastStat.getMaxAvg()),
                        String.format("%.3f", executionLastStat.getAvgMax()),
                        String.format("%.3f", executionLastStat.getAvgAvg()),
                        String.format("%.3f", executionLastStat.getMaxEnergy()),
                        String.format("%.3f", executionLastStat.getAvgEnergy()));
                writer.write(row);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
