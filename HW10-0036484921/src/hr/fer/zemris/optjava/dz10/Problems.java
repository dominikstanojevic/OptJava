package hr.fer.zemris.optjava.dz10;

import hr.fer.zemris.optjava.dz10.models.IFunction;
import hr.fer.zemris.optjava.dz10.models.Pair;
import hr.fer.zemris.optjava.dz10.models.solutions.DoubleArraySolution;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by Dominik on 3.1.2017..
 */
public class Problems {
    public static Pair<IFunction[], Function<Random, DoubleArraySolution>> firstProblem() {
        IFunction[] functions = new IFunction[4];

        functions[0] = new IFunction() {
            @Override
            public double valueAt(double[] vector) {
                return vector[0] * vector[0];
            }

            @Override
            public double getRange() {
                return 10;
            }
        };
        functions[1] = new IFunction() {
            @Override
            public double valueAt(double[] vector) {
                return vector[1] * vector[1];
            }

            @Override
            public double getRange() {
                return 10;
            }
        };
        functions[2] = new IFunction() {
            @Override
            public double valueAt(double[] vector) {
                return vector[2] * vector[2];
            }

            @Override
            public double getRange() {
                return 10;
            }
        };
        functions[3] = new IFunction() {
            @Override
            public double valueAt(double[] vector) {
                return vector[3] * vector[3];
            }

            @Override
            public double getRange() {
                return 10;
            }
        };

        double[] min = new double[4];
        Arrays.fill(min, -5);
        double[] max = new double[4];
        Arrays.fill(max, 5);

        Function<Random, DoubleArraySolution> solutionSupplier = random -> {
            DoubleArraySolution solution = new DoubleArraySolution(4, min, max);
            solution.randomize(random);
            return solution;
        };

        return new Pair<>(functions, solutionSupplier);
    }

    public static Pair<IFunction[], Function<Random, DoubleArraySolution>> secondProblem() {
        IFunction[] functions = new IFunction[2];
        functions[0] = new IFunction() {
            @Override
            public double valueAt(double[] vector) {
                return vector[0];
            }

            @Override
            public double getRange() {
                return 0.9;
            }
        };
        functions[1] = new IFunction() {
            @Override
            public double valueAt(double[] vector) {
                return (1 + vector[1]) / vector[0];
            }

            @Override
            public double getRange() {
                return 59;
            }
        };

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
