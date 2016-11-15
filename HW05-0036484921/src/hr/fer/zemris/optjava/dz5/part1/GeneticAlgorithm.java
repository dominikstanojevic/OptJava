package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.models.IDecoder;
import hr.fer.zemris.optjava.dz5.models.IFunction;
import hr.fer.zemris.optjava.dz5.models.Population;
import hr.fer.zemris.optjava.dz5.models.algorithms.RAPGA;
import hr.fer.zemris.optjava.dz5.models.cfplans.ConstantCompFactorPlan;
import hr.fer.zemris.optjava.dz5.models.cfplans.ICompFactorPlan;
import hr.fer.zemris.optjava.dz5.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz5.models.crossovers.LogicCrossover;
import hr.fer.zemris.optjava.dz5.models.crossovers.NPointCrossover;
import hr.fer.zemris.optjava.dz5.models.mutations.FlipMutation;
import hr.fer.zemris.optjava.dz5.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz5.models.mutations.RandomMutation;
import hr.fer.zemris.optjava.dz5.models.selections.ISelection;
import hr.fer.zemris.optjava.dz5.models.selections.TournamentSelection;
import hr.fer.zemris.optjava.dz5.models.solutions.BitVectorSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Dominik on 11.11.2016..
 */
public class GeneticAlgorithm {
    public static final int MAX_POP = 100;
    public static final int MIN_POP = 10;
    public static final double SUCCESS_RATE = 0.9;
    public static final int MAX_ITERATIONS = 10_000;
    public static final double MAX_SELECTION_PRESS = 100;
    public static final boolean MINIMIZE = false;
    public static final double CONSTANT_PLAN = 1;
    public static final int TOURNAMENT_SIZE = 3;
    public static final double MUTATION_RATE = 0.001;

    //dodan i konstruktor za primanje prosječnog željenog broja mutacija
    public static final int NUMBER_OF_MUTATIONS = 5;

    public static int numberOfBits;
    public static final IFunction FUNCTION = vector -> {
        if (vector[0] <= 0.8 * numberOfBits) {
            return vector[0] / numberOfBits;
        } else if (vector[0] <= 0.9 * numberOfBits) {
            return 0.8;
        } else {
            return (2 * vector[0] / numberOfBits) - 1;
        }
    };

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("One argument expected - number of bits.");
        }
        numberOfBits = Integer.parseInt(args[0]);

        ICompFactorPlan plan = new ConstantCompFactorPlan(CONSTANT_PLAN);
        Function<Random, BitVectorSolution> solutionSupplier = random -> {
            BitVectorSolution solution = new BitVectorSolution(numberOfBits);
            solution.randomize(random);

            return solution;
        };
        IDecoder<BitVectorSolution> decoder = new FirstProblemDecoder();
        ISelection<BitVectorSolution> selection = new TournamentSelection<>(TOURNAMENT_SIZE);

        List<ICrossoverOperator<BitVectorSolution>> crossovers = getCrossovers();
        List<IMutationOperator<BitVectorSolution>> mutations = getMutations();

        RAPGA<BitVectorSolution> algorithm =
                new RAPGA<>(MAX_POP, MIN_POP, SUCCESS_RATE, plan, MAX_SELECTION_PRESS, MAX_ITERATIONS, MINIMIZE,
                        decoder, selection, crossovers, mutations, solutionSupplier, FUNCTION);

        BitVectorSolution solution = algorithm.run();
        System.out.println("Final solution: " + solution);
        System.out.println("k = " + solution.cardinality());
    }

    private static List<IMutationOperator<BitVectorSolution>> getMutations() {
        List<IMutationOperator<BitVectorSolution>> mutations = new ArrayList<>();

        mutations.add(new RandomMutation(NUMBER_OF_MUTATIONS, numberOfBits));
        mutations.add(new FlipMutation(NUMBER_OF_MUTATIONS, numberOfBits));

        return mutations;
    }

    private static List<ICrossoverOperator<BitVectorSolution>> getCrossovers() {
        List<ICrossoverOperator<BitVectorSolution>> crossovers = new ArrayList<>();

        Supplier<BitVectorSolution> supplier = () -> new BitVectorSolution(numberOfBits);

        for (int i = 1; i <= 3; i++) {
            crossovers.add(new NPointCrossover(i, supplier));
        }
        crossovers.add(new LogicCrossover.XORCrossover(supplier));
        crossovers.add(new LogicCrossover.ANDCrossover(supplier));
        crossovers.add(new LogicCrossover.ORCrossover(supplier));

        return crossovers;
    }

}
