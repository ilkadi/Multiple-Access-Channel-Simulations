package org.ehr.simulation;

import org.ehr.algorithm.MoveBigToFront;
import org.ehr.algorithm.RoundRobinWithholding;
import org.ehr.algorithm.SearchRoundRobin;
import org.ehr.channel.IAlgorithm;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        int executionRounds = getExecutionRounds(args);
        int systemSize = getSystemSize(args);
        IAlgorithm algorithm = getAlgorithm(args, systemSize);

        Simulation s = new Simulation(algorithm, executionRounds);

        for(int rhoMultiplier = 1; rhoMultiplier < 20; rhoMultiplier++) {
            double rho = 0.05 * rhoMultiplier;
            s.execute(rho, systemSize);
        }
    }

    private static IAlgorithm getAlgorithm(String[] args, int systemSize) {
        if(Objects.equals(args[0], "RRW"))
            return new RoundRobinWithholding(systemSize);
        else if(Objects.equals(args[0], "MBTF"))
            return new MoveBigToFront(systemSize);
        else
            return new SearchRoundRobin(systemSize);
    }

    private static int getSystemSize(String[] args) {
        return Integer.parseInt(args[1]);
    }

    private static int getExecutionRounds(String[] args) {
        return Integer.parseInt(args[2]);
    }
}
