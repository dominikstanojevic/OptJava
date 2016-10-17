package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.models.FunctionAdder;
import hr.fer.zemris.optjava.dz2.models.IHFunction;
import hr.fer.zemris.optjava.dz2.models.ScalarFunction;
import hr.fer.zemris.optjava.dz2.models.SquareFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.Function;

/**
 * Created by Dominik on 17.10.2016..
 */
public class Prijenosna2 {
    public static final int NUMBER_OF_VARIABLES = 6;

    public static void main(String[] args) {
        Environment.run(args, NUMBER_OF_VARIABLES, 20, 6, row -> createFunction(row), 6);
    }

    private static IHFunction createFunction(double[] row) {
        Function<RealVector, Double> value = vector -> {
            double v = row[0];
            v += vector.getEntry(0) * row[1];
            v += vector.getEntry(1) * Math.pow(row[1], 3) * row[2];
            v += vector.getEntry(2) * Math.exp(vector.getEntry(3) * row[3]) *
                 (1 + Math.cos(vector.getEntry(4) * row[4]));
            v += vector.getEntry(5) * row[4] * row[5] * row[5];

            return v;
        };
        Function<RealVector, Double> function = vector -> Math.pow(value.apply(vector), 2);
        Function<RealVector, RealVector> gradient = vector -> {
            double[] values = new double[6];
            double v = value.apply(vector);

            values[0] = 2 * row[1] * v;
            values[1] = 2 * Math.pow(row[1], 3) * row[2] * v;
            values[2] = 2 * Math.exp(vector.getEntry(3) * row[3]) *
                        (1 + Math.cos(vector.getEntry(4) * row[4])) * v;
            values[3] =
                    2 * vector.getEntry(2) * row[3] * Math.exp(vector.getEntry(3) * row[3]) *
                    (1 + Math.cos(vector.getEntry(4) * row[4])) * v;
            values[4] =
                    -2 * vector.getEntry(2) * row[4] * Math.exp(vector.getEntry(3) * row[3]) *
                    Math.sin(vector.getEntry(4) * row[4]) * v;
            values[5] = 2 * row[4] * Math.pow(row[5], 2) * v;

            return new ArrayRealVector(values);
        };

        Function<RealVector, RealMatrix> hesse = vector -> {
            double[][] values = new double[6][6];

            double help1 = Math.exp(vector.getEntry(3) * row[3]) *
                           Math.cos(vector.getEntry(4) * row[4]);
            double help2 = Math.exp(vector.getEntry(3) * row[3]) *
                           Math.sin(vector.getEntry(4) * row[4]);

            values[0][0] = 2 * row[1] * row[1];
            values[0][1] = 2 * Math.pow(row[1], 2) * row[2];
            values[0][2] = 2 * help1;
            values[0][3] = 2 * help1 * vector.getEntry(2) * row[1] * row[3];
            values[0][4] = -2 * vector.getEntry(2) * row[4] * row[1] * help2;
            values[0][5] = 2 * row[4] * row[5] * row[5] * row[1];

            values[1][0] = values[0][1];
            values[1][1] = 2 * Math.pow(row[1], 6) * row[2] * row[2];
            values[1][2] =
        }

        return new ScalarFunction(NUMBER_OF_VARIABLES, function, gradient, null);
    }
}
