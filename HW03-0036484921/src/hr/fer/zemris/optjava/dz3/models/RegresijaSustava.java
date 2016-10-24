package hr.fer.zemris.optjava.dz3.models;

import hr.fer.zemris.optjava.dz3.models.algorithms.GeometricTempSchedule;
import hr.fer.zemris.optjava.dz3.models.algorithms.GreedyAlgorithm;
import hr.fer.zemris.optjava.dz3.models.algorithms.IOptAlgorithm;
import hr.fer.zemris.optjava.dz3.models.algorithms.ITempSchedule;
import hr.fer.zemris.optjava.dz3.models.algorithms.SimulatedAnnealingAlgorithm;
import hr.fer.zemris.optjava.dz3.models.decoders.GrayBinaryDecoder;
import hr.fer.zemris.optjava.dz3.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz3.models.decoders.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.models.decoders.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.models.neighborhoods.BitVectorNeighborhood;
import hr.fer.zemris.optjava.dz3.models.neighborhoods.DoubleArrayNormNeighborhood;
import hr.fer.zemris.optjava.dz3.models.neighborhoods.DoubleArrayUnifNeighborhood;
import hr.fer.zemris.optjava.dz3.models.neighborhoods.INeighborhood;
import hr.fer.zemris.optjava.dz3.models.solutions.AbstractSolution;
import hr.fer.zemris.optjava.dz3.models.solutions.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.models.solutions.DoubleArraySolution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Dominik on 19.10.2016..
 */
public class RegresijaSustava {
    private static final double DELTA = 0.1;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {
        double[][] data = loadData(args[1], 20, 6);

        IFunction function = vector -> {
            double total = 0;
            for (double[] row : data) {
                double value = row[0];
                value += vector[0] * row[1];
                value += vector[1] * Math.pow(row[1], 3) * row[2];
                value += vector[2] * Math.exp(vector[3] * row[3]) * (1 + Math.cos(vector[4] * row[4]));
                value += vector[5] * row[4] * row[5] * row[5];

                total += value * value;
            }

            return total;
        };

        DoubleArraySolution solution = new DoubleArraySolution(6);
        //BitVectorSolution solution = new BitVectorSolution(6*30);
        double[] min = new double[6];
        Arrays.fill(min, -10);
        double[] max = new double[6];
        Arrays.fill(max, 10);
        solution.randomize(RANDOM, min, max);
        //solution.randomize(RANDOM);

        double[] deltas = new double[6];
        Arrays.fill(deltas, DELTA);

        int[] bits = new int[6];
        Arrays.fill(bits, 30);

        IDecoder<DoubleArraySolution> decoder = new PassThroughDecoder();
        //IDecoder<BitVectorSolution> decoder = new GrayBinaryDecoder(min, max, bits, 6);

        INeighborhood<DoubleArraySolution> neighborhood = new DoubleArrayNormNeighborhood(deltas, RANDOM);
        //INeighborhood<BitVectorSolution> neighborhood = new BitVectorNeighborhood(bits, RANDOM);

        IOptAlgorithm<DoubleArraySolution> algorithm = getAlgorithm(args[0], function, solution, decoder, neighborhood);
        //IOptAlgorithm<BitVectorSolution> algorithm = getAlgorithm(args[0], function, solution, decoder, neighborhood);

        solution = algorithm.run();
        System.out.println(solution);
        double error = calculateError(solution);
        System.out.println("Error: " + error);
    }

    public static <T extends AbstractSolution> double calculateError(T solution) {
        return Math.sqrt(solution.fitness / 6);
    }

    private static <T extends AbstractSolution> IOptAlgorithm<T> getAlgorithm(
            String algorithm, IFunction function, T solution, IDecoder<T> decoder, INeighborhood<T> neighborhood) {
        switch (algorithm) {
            case "greedy":
                return new GreedyAlgorithm<>(decoder, neighborhood, solution, function, true);
            case "annealing":
                ITempSchedule schedule = new GeometricTempSchedule(1000, 5000, 2500);
                return new SimulatedAnnealingAlgorithm<>(decoder, neighborhood, solution, function, true, schedule,
                        RANDOM);
            default:
                throw new IllegalArgumentException("Invalid algorithm");
        }
    }

    private static double[][] loadData(String path, int rows, int columns) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        double[][] values = new double[rows][columns];

        int r = 0;
        for (String l : lines) {
            String line = l.trim();
            if (line.startsWith("#")) {
                continue;
            }

            line = line.replaceAll("\\[", "");
            line = line.replaceAll("\\]", "");
            String[] data = line.split(",");

            double[] row = new double[columns];
            for (int i = 0; i < columns; i++) {
                if (i == columns - 1) {
                    row[0] = -Double.parseDouble(data[i]);
                } else {
                    row[i + 1] = Double.parseDouble(data[i]);
                }
            }

            values[r] = row;
            r++;
        }

        return values;
    }
}
