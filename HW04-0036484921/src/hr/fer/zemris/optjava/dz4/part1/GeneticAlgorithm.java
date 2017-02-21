package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.models.IFunction;
import hr.fer.zemris.optjava.dz4.models.crossovers.DoubleArrayBLXCrossoverOperator;
import hr.fer.zemris.optjava.dz4.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz4.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz4.models.decoders.PassThroughDecoder;
import hr.fer.zemris.optjava.dz4.models.mutations.DoubleArraySolutionNormalMutationOperator;
import hr.fer.zemris.optjava.dz4.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz4.models.selections.TournamentSelection;
import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;
import hr.fer.zemris.optjava.dz4.models.solutions.DoubleArraySolution;
import hr.fer.zemris.optjava.dz4.models.algorithms.GenerationalElitistGA;
import hr.fer.zemris.optjava.dz4.models.selections.ISelection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Entry point for the first part of the homework. Expects one command line
 * argument - path to the file. All other parameters can be set here.
 */
public class GeneticAlgorithm {
    public static final int NUMBER_OF_VARIABLES = 6;
    public static final double[] MIN = new double[NUMBER_OF_VARIABLES];
    public static final double[] MAX = new double[NUMBER_OF_VARIABLES];
    public static final double MIN_BOUND = -5;
    public static final double MAX_BOUND = 5;
    public static final int MAX_ITERATIONS = 10_000;
    public static final double SIGMA = 1e-2;
    public static final double ELITIST_RATE = 0.02;
    public static final double ALPHA = 0.5;
    public static final int TOURNAMENT_SIZE = 10;

    static {
        Arrays.fill(MIN, MIN_BOUND);
        Arrays.fill(MAX, MAX_BOUND);
    }

    public static final int POPULATION_SIZE = 100;
    public static final double MIN_ERROR = 1e-4;

    public static void main(String[] args) throws IOException {
        double[][] data = loadData(args[0], 20, 6);

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

            return Math.sqrt(total / 20);
        };
        IDecoder<DoubleArraySolution> decoder = new PassThroughDecoder();
        Function<Random, DoubleArraySolution> supplier = random -> {
            DoubleArraySolution solution = new DoubleArraySolution(NUMBER_OF_VARIABLES, MIN, MAX);
            solution.randomize(random);
            return solution;
        };

        ICrossoverOperator<DoubleArraySolution> crossoverOperator = new DoubleArrayBLXCrossoverOperator(ALPHA);
        IMutationOperator<DoubleArraySolution> mutationOperator = new DoubleArraySolutionNormalMutationOperator(SIGMA);
        ISelection<DoubleArraySolution> selection = new TournamentSelection<>(TOURNAMENT_SIZE);

        GenerationalElitistGA<DoubleArraySolution> ga =
                new GenerationalElitistGA<>(decoder, crossoverOperator, mutationOperator, POPULATION_SIZE, supplier,
                        MIN_ERROR, MAX_ITERATIONS, selection, ELITIST_RATE, function, true);
        AbstractSolution solution = ga.run();

        System.out.println("----------------------------------------------------");
        System.out.println(solution);
        System.out.println("Error: " + Math.abs(solution.fitness));
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
