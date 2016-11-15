package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.models.IDecoder;
import hr.fer.zemris.optjava.dz5.models.IFunction;
import hr.fer.zemris.optjava.dz5.models.algorithms.SASEGASA;
import hr.fer.zemris.optjava.dz5.models.cfplans.ICompFactorPlan;
import hr.fer.zemris.optjava.dz5.models.cfplans.LogCompFactorPlan;
import hr.fer.zemris.optjava.dz5.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz5.models.crossovers.QAP2PointCrossover;
import hr.fer.zemris.optjava.dz5.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz5.models.mutations.QAPMutation;
import hr.fer.zemris.optjava.dz5.models.selections.ISelection;
import hr.fer.zemris.optjava.dz5.models.selections.TournamentSelection;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Created by Dominik on 12.11.2016..
 */
public class GeneticAlgorithm {
    public static final double SUCCESS_RATIO = 0.9;
    public static final double MAX_SELECTION_PRESS = 400;
    public static final int MAX_ITERATIONS = 750;
    public static final boolean MINIMIZE = true;
    public static final int TOURNAMENT_SIZE = 3;
    public static final int NO_OF_MUTATIONS = 1;
    public static final double LOG_FACTOR = 0.7;
    public static final double MUTATION_RATE = 0;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Invalid number of arguments. Expected: 3, given: " + args.length);
            System.exit(-1);
        }

        Path path = Paths.get(args[0]);
        Data data = getData(path);

        IFunction function = vector -> {
            double total = 0;

            for (int i = 0; i < data.size; i++) {
                for (int j = 0; j < data.size; j++) {
                    int pi = (int) Math.round(vector[i]);
                    int pj = (int) Math.round(vector[j]);

                    total += data.costs[i][j] * data.distances[pi][pj];
                }
            }

            return total;
        };

        int populationSize = Integer.parseInt(args[1]);
        int noOfVillages = Integer.parseInt(args[2]);

        ICompFactorPlan plan = new LogCompFactorPlan(MAX_ITERATIONS, LOG_FACTOR);

        Function<Random, QAPSolution> supplier = random -> {
            QAPSolution solution = new QAPSolution(data.size);
            solution.randomize(random);

            return solution;
        };

        IDecoder<QAPSolution> decoder = new QAPDecoder();
        ISelection<QAPSolution> selection = new TournamentSelection<>(TOURNAMENT_SIZE);
        ICrossoverOperator<QAPSolution> crossover = new QAP2PointCrossover(() -> new QAPSolution(data.size));
        IMutationOperator<QAPSolution> mutation = new QAPMutation(MUTATION_RATE, NO_OF_MUTATIONS);

        SASEGASA<QAPSolution> ga =
                new SASEGASA<>(populationSize, noOfVillages, SUCCESS_RATIO, MAX_SELECTION_PRESS, MAX_ITERATIONS, plan,
                        function, MINIMIZE, supplier, decoder, selection, crossover, mutation);

        QAPSolution best = ga.run();
        System.out.println(best);
        System.out.println("Value: " + function.valueAt(decoder.decode(best)));

    }

    private static Data getData(Path path) throws IOException {
        Scanner sc = new Scanner(path);
        int size = sc.nextInt();

        double[][] distances = readSquareTable(sc, size);
        double[][] costs = readSquareTable(sc, size);

        return new Data(size, distances, costs);
    }

    private static double[][] readSquareTable(Scanner sc, int size) {
        double[][] array = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                array[i][j] = sc.nextDouble();
            }
        }

        return array;
    }

    private static class Data {
        public int size;
        public double[][] distances;
        public double[][] costs;

        public Data(int size, double[][] distances, double[][] costs) {
            this.size = size;
            this.distances = distances;
            this.costs = costs;
        }
    }
}
