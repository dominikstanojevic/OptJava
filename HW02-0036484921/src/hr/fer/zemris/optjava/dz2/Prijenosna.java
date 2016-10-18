package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.models.FunctionAdder;
import hr.fer.zemris.optjava.dz2.models.IHFunction;
import hr.fer.zemris.optjava.dz2.models.ScalarFunction;
import hr.fer.zemris.optjava.dz2.models.SquareFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.Function;

public class Prijenosna {
    public static final int NUMBER_OF_VARIABLES = 6;

    public static void main(String[] args) {
        Environment.run(args, NUMBER_OF_VARIABLES, 20, 6, row -> createFunction(row), 6);
    }

    private static IHFunction createFunction(double[] row) {
        FunctionAdder fc = new FunctionAdder(6);
        fc.addFunction(createConstant(row));
        fc.addFunction(createFirstFunction(row));
        fc.addFunction(createSecondFunction(row));
        fc.addFunction(createThirdFunction(row));
        fc.addFunction(createFourthFunction(row));

        return new SquareFunction(fc);
    }

    private static IHFunction createFirstFunction(double[] row) {
        Function<RealVector, Double> function = vector -> vector.getEntry(0) * row[1];
        Function<RealVector, RealVector> gradient = vector -> {
            double[] values = new double[6];
            values[0] = row[1];

            return new ArrayRealVector(values);
        };

        Function<RealVector, RealMatrix> hessian =
                vector -> MatrixUtils.createRealMatrix(6, 6);

        return new ScalarFunction(6, function, gradient, hessian);
    }

    private static IHFunction createSecondFunction(double[] row) {
        Function<RealVector, Double> function =
                vector -> vector.getEntry(1) * Math.pow(row[1], 3) * row[2];
        Function<RealVector, RealVector> gradient = vector -> {
            double[] values = new double[6];
            values[1] = Math.pow(row[1], 3) * row[2];

            return new ArrayRealVector(values);
        };

        Function<RealVector, RealMatrix> hessian =
                vector -> MatrixUtils.createRealMatrix(6, 6);

        return new ScalarFunction(6, function, gradient, hessian);
    }

    private static IHFunction createThirdFunction(double[] row) {
        Function<RealVector, Double> function =
                vector -> vector.getEntry(2) * Math.exp(vector.getEntry(3) * row[3]) *
                          (1 + Math.cos(vector.getEntry(4) * row[4]));
        Function<RealVector, RealVector> gradient = vector -> {
            double[] values = new double[6];

            values[2] = Math.exp(vector.getEntry(3) * row[3]) *
                        (1 + Math.cos(vector.getEntry(4) * row[4]));
            values[3] = vector.getEntry(2) * row[3] * values[2];
            values[4] = -vector.getEntry(2) * row[4] * Math.exp(vector.getEntry(3) * row[3]) *
                        Math.sin(vector.getEntry(4) * row[4]);

            return new ArrayRealVector(values);
        };

        Function<RealVector, RealMatrix> hessian = vector -> {
            double[][] values = new double[6][6];

            values[2][3] = row[3] * Math.exp(vector.getEntry(3) * row[3]) *
                           (1 + Math.cos(vector.getEntry(4) * row[4]));
            values[2][4] = -row[4] * Math.exp(vector.getEntry(3) * row[3]) *
                           Math.sin(vector.getEntry(4) * row[4]);

            values[3][2] = values[2][3];
            values[3][3] = vector.getEntry(2) * Math.pow(row[3], 2) *
                           Math.exp(vector.getEntry(3) * row[3]) *
                           (1 + Math.cos(vector.getEntry(4) * row[4]));
            values[3][4] = -vector.getEntry(2) * row[3] * row[4] *
                           Math.exp(vector.getEntry(3) * row[3]) *
                           Math.sin(vector.getEntry(4) * row[4]);

            values[4][2] = values[2][4];
            values[4][3] = values[3][4];
            values[4][4] = -vector.getEntry(2) * Math.pow(row[4], 2) *
                           Math.exp(vector.getEntry(3) * row[3]) *
                           Math.cos(vector.getEntry(4) * row[4]);

            return MatrixUtils.createRealMatrix(values);
        };

        return new ScalarFunction(6, function, gradient, hessian);
    }

    private static IHFunction createFourthFunction(double[] row) {
        Function<RealVector, Double> function =
                vector -> vector.getEntry(5) * row[4] * Math.pow(row[5], 2);
        Function<RealVector, RealVector> gradient = vector -> {
            double[] values = new double[6];

            values[5] = row[4] * Math.pow(row[5], 2);

            return new ArrayRealVector(values);
        };
        Function<RealVector, RealMatrix> hessian =
                vector -> MatrixUtils.createRealMatrix(6, 6);

        return new ScalarFunction(6, function, gradient, hessian);
    }

    private static IHFunction createConstant(double[] row) {
        Function<RealVector, Double> function = vector -> row[0];
        Function<RealVector, RealVector> gradient = vector -> new ArrayRealVector(6);
        Function<RealVector, RealMatrix> hessian =
                vector -> MatrixUtils.createRealMatrix(6, 6);
        return new ScalarFunction(6, function, gradient, hessian);
    }
}
