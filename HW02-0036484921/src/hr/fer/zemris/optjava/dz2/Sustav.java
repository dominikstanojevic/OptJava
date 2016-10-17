package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.models.FunctionAdder;
import hr.fer.zemris.optjava.dz2.models.IHFunction;
import hr.fer.zemris.optjava.dz2.models.ScalarFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.Function;

/**
 * Created by Dominik on 13.10.2016..
 */
public class Sustav {
    public static void main(String[] args) {
        Environment.run(args, 10, 10, 11, row -> createFunction(row), 10);
    }

    private static IHFunction createFunction(double[] row) {
        Function<RealVector, Double> value = vector -> {
            double result = row[0];
            for (int i = 0, len = vector.getDimension(); i < len; i++) {
                result += row[i + 1] * vector.getEntry(i);
            }
            return result;
        };

        Function<RealVector, Double> function = vector -> Math.pow(value.apply(vector), 2);
        Function<RealVector, RealVector> gradient = vector -> {
            double v = value.apply(vector);

            int size = vector.getDimension();
            RealVector g = new ArrayRealVector(size);
            for (int i = 0; i < size; i++) {
                double entry = 2 * v * row[i + 1];
                g.setEntry(i, entry);
            }

            return g;
        };

        Function<RealVector, RealMatrix> hessian = vector -> {
            int size = vector.getDimension();
            double[][] m = new double[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    m[i][j] = 2 * row[i + 1] * row[j + 1];
                }
            }

            return MatrixUtils.createRealMatrix(m);
        };

        ScalarFunction f = new ScalarFunction(row.length - 1, function, gradient, hessian);
        return f;
    }
}
