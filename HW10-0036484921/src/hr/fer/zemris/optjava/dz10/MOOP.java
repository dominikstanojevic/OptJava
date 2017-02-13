package hr.fer.zemris.optjava.dz10;

import hr.fer.zemris.optjava.dz10.models.IFunction;
import hr.fer.zemris.optjava.dz10.models.Pair;
import hr.fer.zemris.optjava.dz10.models.Population;
import hr.fer.zemris.optjava.dz10.models.algorithms.IMOOPProblem;
import hr.fer.zemris.optjava.dz10.models.algorithms.NSGAII;
import hr.fer.zemris.optjava.dz10.models.algorithms.RealValuedMOOPProblem;
import hr.fer.zemris.optjava.dz10.models.crossovers.DoubleArrayBLXCrossoverOperator;
import hr.fer.zemris.optjava.dz10.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz10.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz10.models.decoders.PassThroughDecoder;
import hr.fer.zemris.optjava.dz10.models.mutations.DoubleArraySolutionNormalMutationOperator;
import hr.fer.zemris.optjava.dz10.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz10.models.selections.ISelection;
import hr.fer.zemris.optjava.dz10.models.selections.TournamentSelection;
import hr.fer.zemris.optjava.dz10.models.solutions.DoubleArraySolution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * Created by Dominik on 29.12.2016..
 */
public class MOOP {
    public static final double MUTATION_SIGMA = 0.01;
    public static double CROSSOVER_ALPHA = 0.5;
    public static final int TOURNAMENT_SIZE = 3;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Invalid number of arguments. Given: " + args.length + " expected 3.");
            System.exit(-1);
        }
        int problem = Integer.parseInt(args[0].trim());
        int populationSize = Integer.parseInt(args[1].trim());
        int maxGenerations = Integer.parseInt(args[2].trim());

        Pair<IFunction[], Function<Random, DoubleArraySolution>> problemArguments = chooseProblem(problem);

        IDecoder<DoubleArraySolution> decoder = new PassThroughDecoder();
        IMOOPProblem<DoubleArraySolution> evaluation = new RealValuedMOOPProblem(problemArguments.first);

        IMutationOperator<DoubleArraySolution> mutationOperator =
                new DoubleArraySolutionNormalMutationOperator(MUTATION_SIGMA);
        ICrossoverOperator<DoubleArraySolution> crossoverOperator =
                new DoubleArrayBLXCrossoverOperator(CROSSOVER_ALPHA);
        ISelection<DoubleArraySolution> tournament = new TournamentSelection<>(TOURNAMENT_SIZE);

        NSGAII<DoubleArraySolution> nsgaii =
                new NSGAII<>(maxGenerations, populationSize, evaluation, decoder, problemArguments.second, tournament,
                        crossoverOperator, mutationOperator);
        List<Population<DoubleArraySolution>> fronts = nsgaii.run();

        System.out.println("Non-dominated front:");
        printResults(fronts.get(0), evaluation);
    }

    private static Pair<IFunction[], Function<Random, DoubleArraySolution>> chooseProblem(int problem) {
        switch (problem) {
            case 1:
                CROSSOVER_ALPHA = 0;
                return Problems.firstProblem();
            case 2:
                return Problems.secondProblem();
            default:
                throw new IllegalArgumentException("Invalid problem number. Choose problem 1 or problem 2.");
        }
    }

    private static void printResults(
            Population<DoubleArraySolution> front, IMOOPProblem<DoubleArraySolution> evaluator) throws IOException {
        StringJoiner values = new StringJoiner("\n");
        StringJoiner f = new StringJoiner("\n");
        for (DoubleArraySolution solution : front) {
            double[] results = evaluator.evaluateSolution(solution);

            StringJoiner inner = new StringJoiner(",");
            for (double value : solution.chromosome) {
                inner.add(Double.toString(value));
            }
            values.add(inner.toString());

            inner = new StringJoiner(",");
            for (double result : results) {
                inner.add(Double.toString(result));
            }
            f.add(inner.toString());

            String s = Arrays.toString(solution.chromosome) + " -> " + Arrays.toString(results) + "\n";
            System.out.print(s);
        }

        Files.write(Paths.get("datoteke\\izlaz-dec.txt"),
                values.toString().getBytes());
        Files.write(Paths.get("datoteke\\izlaz-obj.txt"),
                f.toString().getBytes());
    }
}
