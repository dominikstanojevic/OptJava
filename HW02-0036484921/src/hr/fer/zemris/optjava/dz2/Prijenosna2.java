package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.models.IHFunction;
import hr.fer.zemris.optjava.dz2.models.ScalarFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
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

            double v = value.apply(vector);

            double c = vector.getEntry(2);
            double v2 = row[5] * row[5];

            double exp = Math.exp(vector.getEntry(3) * row[3]);
            double cos = Math.cos(vector.getEntry(4) * row[4]);
            double sin = Math.sin(vector.getEntry(4) * row[4]);

            values[0][0] = 2 * row[1] * row[1];
            values[0][1] = 2 * Math.pow(row[1], 4) * row[2];
            values[0][2] = 2 * exp * row[1] * (cos + 1);
            values[0][3] = 2 * c * exp * row[1] * row[3] * (cos + 1);
            values[0][4] = -2 * c * exp * row[4] * row[1] * sin;
            values[0][5] = 2 * row[4] * v2 * row[1];

            double x3 = Math.pow(row[1], 3);
            values[1][0] = values[0][1];
            values[1][1] = 2 * x3 * x3 * row[2] * row[2];
            values[1][2] = 2 * exp * x3 * row[2] * (cos + 1);
            values[1][3] = 2 * c * exp * x3 * row[2] * row[3] * (cos + 1);
            values[1][4] = -2 * c * exp * row[4] * x3 * row[2] * sin;
            values[1][5] = 2 * row[4] * v2 * x3 * row[2];

            double exp2 = exp * exp;
            double cos2 = (cos + 1) * (cos + 1);
            values[2][0] = values[0][2];
            values[2][1] = values[1][2];
            values[2][2] = 2 * exp2 * cos2;
            values[2][3] = 2 * c * exp2 * row[3] * cos2 + 2 * exp * row[3] * v * (cos + 1);
            values[2][4] =
                    -2 * c * exp2 * row[4] * (cos + 1) * sin - 2 * exp * row[4] * v * sin;
            values[2][5] = 2 * exp * row[4] * v2 * (cos + 1);

            double z2 = row[3] * row[3];
            values[3][0] = values[0][3];
            values[3][1] = values[1][3];
            values[3][2] = values[2][3];
            values[3][3] = 2 * c * c * exp2 * cos2 * z2 + 2 * c * exp * (cos + 1) * v * z2;
            values[3][4] = -2 * exp2 * row[4] * row[3] * (cos + 1) * sin * c * c -
                           2 * exp * row[4] * row[3] * v * sin * c;
            values[3][5] = 2 * c * exp * row[4] * v2 * row[3] * (cos + 1);

            values[4][0] = values[0][4];
            values[4][1] = values[1][4];
            values[4][2] = values[2][4];
            values[4][3] = values[3][4];
            values[4][4] = 2 * c * c * exp2 * row[4] * row[4] * sin * sin -
                           2 * c * exp * row[4] * row[4] * cos * v;
            values[4][5] = -2 * c * exp * row[4] * row[4] * v2 * sin;

            values[5][0] = values[0][5];
            values[5][1] = values[1][5];
            values[5][2] = values[2][5];
            values[5][3] = values[3][5];
            values[5][4] = values[4][5];
            values[5][5] = 2 * row[4] * row[4] * v2 * v2;

            return MatrixUtils.createRealMatrix(values);
        };

        return new ScalarFunction(NUMBER_OF_VARIABLES, function, gradient, hesse);
    }
}
