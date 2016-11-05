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
 * Needed command-line parameters are: [METHOD] [ALGORITHM] [PATH]
 * Method - "decimal" or "binary" (binary uses Gray code)
 * Algorithm - "greedy" or "annealing"
 * Path - path to the file
 *
 * @author Dominik Stanojević
 */
public class RegresijaSustava {
    private static final Random RANDOM = new Random();

    public static final int NUMBER_OF_VARIABLES = 6;
    public static final double DELTA = 0.1;
    public static final int VECTOR_SIZE = 30; //only for bitvectors
    public static final double MIN = -10;
    public static final double MAX = 10;

    public static final double ALPHA = 0.992; //not necessary
    public static final double STARTING_TEMPERATURE = 1000;
    public static final int INNER_LOOPS = 5000;
    public static final int OUTER_LOOPS = 2500;

    public static void main(String[] args) throws IOException {
        double[][] data = loadData(args[2], 20, 6);

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

        IOptAlgorithm<? extends AbstractSolution> algorithm = getAlgorithm(args[0], args[1], function);
        AbstractSolution solution = algorithm.run();
        
        System.out.println(solution);
        double error = calculateError(solution);
        System.out.println("Error: " + error);
    }

    public static <T extends AbstractSolution> double calculateError(T solution) {
        return Math.sqrt(Math.abs(solution.fitness) / 20);
    }

    private static IOptAlgorithm<? extends AbstractSolution> getAlgorithm(
            String method, String algorithm, IFunction function) {
        double[] min = new double[NUMBER_OF_VARIABLES];
        Arrays.fill(min, MIN);
        double[] max = new double[NUMBER_OF_VARIABLES];
        Arrays.fill(max, MAX);

        double[] deltas = new double[NUMBER_OF_VARIABLES];
        Arrays.fill(deltas, DELTA);

        switch (method) {
            case "decimal":
                DoubleArraySolution solutionDA = new DoubleArraySolution(6);
                solutionDA.randomize(RANDOM, min, max);

                IDecoder<DoubleArraySolution> decoderDA = new PassThroughDecoder();
                INeighborhood<DoubleArraySolution> neighborhoodDA = new DoubleArrayNormNeighborhood(deltas, RANDOM);
                return getAlgorithm(algorithm, function, solutionDA, decoderDA, neighborhoodDA);
            case "binary":
                BitVectorSolution solutionBV = new BitVectorSolution(NUMBER_OF_VARIABLES * VECTOR_SIZE);
                solutionBV.randomize(RANDOM);

                int[] bits = new int[NUMBER_OF_VARIABLES];
                Arrays.fill(bits, VECTOR_SIZE);

                IDecoder<BitVectorSolution> decoderBV = new GrayBinaryDecoder(min, max, bits, NUMBER_OF_VARIABLES);
                INeighborhood<BitVectorSolution> neighborhoodBV = new BitVectorNeighborhood(bits, RANDOM);
                return getAlgorithm(algorithm, function, solutionBV, decoderBV, neighborhoodBV);
            default:
                throw new IllegalArgumentException("Method does not exist.");
        }
    }

    private static <T extends AbstractSolution> IOptAlgorithm<T> getAlgorithm(
            String algorithm, IFunction function, T solution, IDecoder<T> decoder, INeighborhood<T> neighborhood) {
        switch (algorithm) {
            case "greedy":
                return new GreedyAlgorithm<>(decoder, neighborhood, solution, function, true);
            case "annealing":
                ITempSchedule schedule = new GeometricTempSchedule(STARTING_TEMPERATURE, INNER_LOOPS, OUTER_LOOPS);
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
