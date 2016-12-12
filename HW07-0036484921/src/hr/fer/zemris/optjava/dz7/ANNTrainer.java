package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.algorithms.CLONALG;
import hr.fer.zemris.optjava.dz7.algorithms.PSO;
import hr.fer.zemris.optjava.dz7.models.Antibody;
import hr.fer.zemris.optjava.dz7.models.neural.ActivationFunction;
import hr.fer.zemris.optjava.dz7.models.neural.FFANN;
import hr.fer.zemris.optjava.dz7.models.neural.Weights;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Dominik on 7.12.2016..
 */
public class ANNTrainer {
    public static ThreadLocal<FFANN> network;

    public static final double LOCAL_COEFFICIENT = 1.5;
    public static final double PERSONAL_COEFFICIENT = 1.5;
    public static final double MIN_INERTIA = 0.25;
    public static final double MAX_INERTIA = 0.75;

    public static final double LOWER_BOUND = -1;
    public static final double UPPER_BOUND = 1;
    public static final double VELOCITY_RANGE = 2;

    public static int numberOfClones;
    public static final double GENERATED_ANTIBODIES_PERCENTAGE = 0.1;
    public static final double RO = 2;
    public static final double SIGMA = 0.25;

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.err.println("Invalid number of arguments. Given: " + args.length + ", expected 5.");
            System.exit(-1);
        }

        String path = args[0];
        String algorithm = args[1].trim();
        int populationSize = numberOfClones = Integer.parseInt(args[2]);
        double merr = Double.parseDouble(args[3]);
        int maxIterations = Integer.parseInt(args[4]);

        Dataset dataset = Utils.loadData(path);

        network = ThreadLocal.withInitial(() -> new FFANN(new int[] {
                dataset.inputs.getColumnDimension(), 5, dataset.outputs.getColumnDimension() },
                new ActivationFunction[] {
                        ActivationFunction.SIGMOID, ActivationFunction.SIGMOID }, dataset));

        double[] lowerBound = new double[network.get().getNumberOfWeights()];
        double[] upperBound = new double[lowerBound.length];

        Arrays.fill(lowerBound, LOWER_BOUND);
        Arrays.fill(upperBound, UPPER_BOUND);

        double[] velocity = new double[lowerBound.length];
        Arrays.fill(velocity, VELOCITY_RANGE);

        Weights best = null;
        if (algorithm.equals("pso-a")) {
            PSO pso = new PSO(populationSize, PERSONAL_COEFFICIENT, LOCAL_COEFFICIENT, maxIterations, MIN_INERTIA,
                    MAX_INERTIA, network.get().getNumberOfWeights(), lowerBound, upperBound, velocity, merr);
            best = pso.run();
        } else if (algorithm.startsWith("pso-b-")) {
            int halfNeighborhood = Integer.parseInt(algorithm.replace("pso-b-", "").trim());
            PSO pso = new PSO(populationSize, halfNeighborhood, PERSONAL_COEFFICIENT, LOCAL_COEFFICIENT, maxIterations,
                    MIN_INERTIA, MAX_INERTIA, network.get().getNumberOfWeights(), lowerBound, upperBound, velocity,
                    merr);
            best = pso.run();
        } else if (algorithm.equals("clonalg")) {
            Supplier<Antibody> antibodySupplier = () -> {
                Antibody antibody = new Antibody(network.get().getNumberOfWeights());
                antibody.randomize(lowerBound, upperBound);
                return antibody;
            };

            Predicate<Antibody> stopCondition = x -> 1. / x.affinity < merr;
            CLONALG clonalg =
                    new CLONALG(populationSize, numberOfClones, GENERATED_ANTIBODIES_PERCENTAGE, maxIterations,
                            stopCondition, antibodySupplier, RO, SIGMA);

            best = clonalg.run();
        } else {
            System.err.println("Invalid algorithm. Given: " + algorithm);
        }

        System.out.println("Best: " + best.toString());
        double percentage = network.get().test(best.getWeights()) * 100;
        System.out.println("Percentage: " + percentage + "%.");
    }
}
