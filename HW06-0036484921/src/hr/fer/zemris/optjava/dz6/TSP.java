package hr.fer.zemris.optjava.dz6;

import hr.fer.zemris.optjava.dz6.models.Ant;
import hr.fer.zemris.optjava.dz6.models.CityMap;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Dominik on 1.12.2016..
 */
public class TSP {
    public static final double BETA = 2;
    public static final double ALPHA = 1;
    public static final double RO = 0.98;

    public static final ExecutorService POOL =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.err.println("Invalid number of arguments. Given: " + args.length + ", expected 4.");
            System.exit(-1);
        }

        String path = args[0];
        int neighborhoodSize = Integer.parseInt(args[1]);
        int colonySize = Integer.parseInt(args[2]);
        int maxIterations = Integer.parseInt(args[3]);

        CityMap map = CityMap.loadFromFile(path, neighborhoodSize);

        MMASTSP algorithm = new MMASTSP(map, colonySize, ALPHA, BETA, RO, maxIterations);
        Ant best = algorithm.run();

        System.out.println("Best ant: ");
        System.out.println(best);

        POOL.shutdownNow();
    }
}
