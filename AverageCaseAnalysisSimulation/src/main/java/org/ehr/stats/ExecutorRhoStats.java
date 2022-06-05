package org.ehr.stats;

public class ExecutorRhoStats {
    private final double rho;
    private final double[] maxMax;
    private final double[] avgMax;
    private final double[] maxAvg;
    private final double[] avgAvg;
    private final double[] maxEnergy;
    private final double[] avgEnergy;

    public ExecutorRhoStats(double rho, int executionRounds) {
        this.rho = rho;
        this.maxMax = new double[executionRounds];
        this.avgMax = new double[executionRounds];
        this.maxAvg = new double[executionRounds];
        this.avgAvg = new double[executionRounds];
        this.maxEnergy = new double[executionRounds];
        this.avgEnergy = new double[executionRounds];
    }

    public double getRho() {
        return rho;
    }

    public double[] getMaxMax() {
        return maxMax;
    }

    public double[] getAvgMax() {
        return avgMax;
    }

    public double[] getMaxAvg() {
        return maxAvg;
    }

    public double[] getAvgAvg() {
        return avgAvg;
    }

    public double[] getMaxEnergy() {
        return maxEnergy;
    }

    public double[] getAvgEnergy() {
        return avgEnergy;
    }
}
