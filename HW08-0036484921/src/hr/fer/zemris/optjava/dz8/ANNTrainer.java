package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.algorithms.DE;
import hr.fer.zemris.optjava.dz8.models.Dataset;
import hr.fer.zemris.optjava.dz8.models.Evaluator;
import hr.fer.zemris.optjava.dz8.models.Solution;
import hr.fer.zemris.optjava.dz8.models.neural.AbstractANN;
import hr.fer.zemris.optjava.dz8.models.neural.ActivationFunction;
import hr.fer.zemris.optjava.dz8.models.neural.ElmanNeuralNetwork;
import hr.fer.zemris.optjava.dz8.models.neural.FFANN;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Created by Dominik on 19.12.2016..
 */
public class ANNTrainer {
    public static final int SERIES_SIZE = 600;
    public static final double SCALE_FACTOR = 0.5;
    public static final double MUTATION_RATE = 0.005;

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.err.println("Invalid number of arguments. Expected 5, given: " + args.length);
            System.exit(-1);
        }

        String path = args[0];
        Supplier<AbstractANN> network = getNetwork(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        double merr = Double.parseDouble(args[3]);
        int maxIterations = Integer.parseInt(args[4]);

        Dataset data = Dataset.loadData(path, network.get().numberOfInputNeurons(), SERIES_SIZE);

        Evaluator evaluator = new Evaluator(network, data);

        DE de = new DE(evaluator.numberOfParameters(), populationSize, maxIterations, merr, evaluator, SCALE_FACTOR,
                MUTATION_RATE);
        Solution best = de.run();
        System.out.println("------------------------------");
        System.out.println("Best: " + 1. / best.fitness);
    }

    private static Supplier<AbstractANN> getNetwork(String string) {
        if (string.startsWith("tdnn-")) {
            string = string.replace("tdnn-", "");
            int[] numberOfNeurons = getNumberOfNeurons(string);
            ActivationFunction[] functions = new ActivationFunction[numberOfNeurons.length - 1];
            Arrays.fill(functions, ActivationFunction.HYP_TAN);

            return () -> new FFANN(numberOfNeurons, functions);
        } else if (string.startsWith("elman-")) {
            string = string.replace("elman-", "");
            int[] numberOfNeurons = getNumberOfNeurons(string);
            ActivationFunction[] functions = new ActivationFunction[numberOfNeurons.length - 1];
            Arrays.fill(functions, ActivationFunction.HYP_TAN);

            return () -> new ElmanNeuralNetwork(numberOfNeurons, functions);
        } else {
            throw new RuntimeException("Invalid name for neural network. Given: " + string);
        }
    }

    private static int[] getNumberOfNeurons(String string) {
        String[] data = string.trim().split("x");
        return Arrays.stream(data).mapToInt(Integer::parseInt).toArray();
    }
}
