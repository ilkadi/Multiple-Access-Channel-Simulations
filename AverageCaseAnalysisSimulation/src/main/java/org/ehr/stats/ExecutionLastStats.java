package org.ehr.stats;

public class ExecutionLastStats {
    private final double rho;
    private final double maxMax;
    private final double avgMax;
    private final double maxAvg;
    private final double avgAvg;
    private final double maxEnergy;
    private final double avgEnergy;

    public ExecutionLastStats(double rho,
                              double maxMax, double avgMax,
                              double maxAvg, double avgAvg,
                              double maxEnergy, double avgEnergy) {
        this.rho = rho;
        this.maxMax = maxMax;
        this.avgMax = avgMax;
        this.maxAvg = maxAvg;
        this.avgAvg = avgAvg;
        this.maxEnergy = maxEnergy;
        this.avgEnergy = avgEnergy;
    }

    public double getRho() {
        return rho;
    }

    public double getMaxMax() {
        return maxMax;
    }

    public double getAvgMax() {
        return avgMax;
    }

    public double getMaxAvg() {
        return maxAvg;
    }

    public double getAvgAvg() {
        return avgAvg;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public double getAvgEnergy() {
        return avgEnergy;
    }
}
