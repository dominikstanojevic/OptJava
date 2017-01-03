package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.models.IFunction;
import hr.fer.zemris.optjava.dz9.models.Pair;
import hr.fer.zemris.optjava.dz9.models.Population;
import hr.fer.zemris.optjava.dz9.models.algorithms.Distance;
import hr.fer.zemris.optjava.dz9.models.algorithms.IMOOPProblem;
import hr.fer.zemris.optjava.dz9.models.algorithms.NSGA;
import hr.fer.zemris.optjava.dz9.models.algorithms.RealValuedMOOPProblem;
import hr.fer.zemris.optjava.dz9.models.crossovers.DoubleArrayBLXCrossoverOperator;
import hr.fer.zemris.optjava.dz9.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz9.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz9.models.decoders.PassThroughDecoder;
import hr.fer.zemris.optjava.dz9.models.mutations.DoubleArraySolutionNormalMutationOperator;
import hr.fer.zemris.optjava.dz9.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz9.models.solutions.DoubleArraySolution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * Created by Dominik on 29.12.2016..
 */
public class MOOP {
    public static final double MUTATION_SIGMA = 0.01;
    public static double CROSSOVER_ALPHA = 0.5;
    public static final double NICHE_RADIUS = 1;
    public static final double ALPHA = 2;

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.err.println("Invalid number of arguments. Given: " + args.length + " expected 4.");
            System.exit(-1);
        }
        int problem = Integer.parseInt(args[0].trim());
        int populationSize = Integer.parseInt(args[1].trim());
        String space = args[2].trim();
        int maxGenerations = Integer.parseInt(args[3].trim());

        Pair<IFunction[], Function<Random, DoubleArraySolution>> problemArguments = chooseProblem(problem);
        Distance distanceType = chooseSpace(space);

        IDecoder<DoubleArraySolution> decoder = new PassThroughDecoder();
        IMOOPProblem<DoubleArraySolution> evaluation = new RealValuedMOOPProblem(problemArguments.first);

        IMutationOperator<DoubleArraySolution> mutationOperator =
                new DoubleArraySolutionNormalMutationOperator(MUTATION_SIGMA);
        ICrossoverOperator<DoubleArraySolution> crossoverOperator =
                new DoubleArrayBLXCrossoverOperator(CROSSOVER_ALPHA);

        NSGA<DoubleArraySolution> nsga =
                new NSGA<>(decoder, evaluation, problemArguments.second, mutationOperator, crossoverOperator,
                        distanceType, maxGenerations, populationSize, NICHE_RADIUS, ALPHA);
        Population<DoubleArraySolution> solution = nsga.run();

        printResults(solution, evaluation);
    }

    private static Distance chooseSpace(String space) {
        switch (space.toLowerCase()) {
            case "decision-space":
                return Distance.DECISION_SPACE;
            case "objective-space":
                return Distance.OBJECTIVE_SPACE;
            default:
                throw new IllegalArgumentException(
                        "Invalid space: " + space + ". Choose decision-space or " + "objective-space.");
        }
    }

    private static Pair<IFunction[], Function<Random, DoubleArraySolution>> chooseProblem(int problem) {
        switch (problem) {
            case 1:
                return firstProblem();
            case 2:
                return secondProblem();
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

        Files.write(Paths.get("D:\\fer\\5. semestar\\ROPAERUJ\\workspace\\HW09-0036484921\\datoteke\\izlaz-dec.txt"),
                values.toString().getBytes());
        Files.write(Paths.get("D:\\fer\\5. semestar\\ROPAERUJ\\workspace\\HW09-0036484921\\datoteke\\izlaz-obj.txt"),
                f.toString().getBytes());
    }

    private static Pair<IFunction[], Function<Random, DoubleArraySolution>> firstProblem() {
        IFunction[] functions = new IFunction[4];

        functions[0] = vector -> vector[0] * vector[0];
        functions[1] = vector -> vector[1] * vector[1];
        functions[2] = vector -> vector[2] * vector[2];
        functions[3] = vector -> vector[3] * vector[3];

        double[] min = new double[4];
        Arrays.fill(min, -5);
        double[] max = new double[4];
        Arrays.fill(max, 5);

        Function<Random, DoubleArraySolution> solutionSupplier = random -> {
            DoubleArraySolution solution = new DoubleArraySolution(4, min, max);
            solution.randomize(random);
            return solution;
        };

        CROSSOVER_ALPHA = 0;

        return new Pair<>(functions, solutionSupplier);
    }

    private static Pair<IFunction[], Function<Random, DoubleArraySolution>> secondProblem() {
        IFunction[] functions = new IFunction[2];
        functions[0] = vector -> vector[0];
        functions[1] = vector -> (1 + vector[1]) / vector[0];

        double[] min = new double[] { 0.1, 0 };
        double[] max = new double[] { 1, 5 };

        Function<Random, DoubleArraySolution> solutionSupplier = random -> {
            DoubleArraySolution solution = new DoubleArraySolution(2, min, max);
            solution.randomize(random);
            return solution;
        };

        return new Pair<>(functions, solutionSupplier);
    }
}
