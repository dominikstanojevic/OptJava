package hr.fer.zemris.optjava.dz2.assignments;

import hr.fer.zemris.optjava.dz2.algorithms.NumOptAlgorithms;
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
public class FirstFunction {
    public static void main(String[] args) {
        Function<RealVector, Double> f = vector -> vector.getEntry(0) * vector.getEntry(0) -
                                                   Math.pow((vector.getEntry(1) - 1), 2);
        Function<RealVector, RealVector> gradient = vector -> {
            double firstResult = 2 * vector.getEntry(0);
            double secondResult = 2 * (vector.getEntry(1) - 1);

            return new ArrayRealVector(new double[] { firstResult, secondResult });
        };
        Function<RealVector, RealMatrix> hessian =
                vector -> MatrixUtils.createRealMatrix(new double[][] { { 2, 0 }, { 0, 2 } });

        IHFunction function = new ScalarFunction(2, f, gradient, hessian);
        RealVector result;
        result = NumOptAlgorithms
                .gradientDescentAlgorithm(function, 100_000, function.getNumberOfVariables());
        printGradientValue(function, result);
        System.out.println("----------------------------------------------------");
        result = NumOptAlgorithms
                .newtonAlgorithm(function, 100_000, function.getNumberOfVariables());
        printGradientValue(function, result);

    }

    public static void printGradientValue(IHFunction function, RealVector result) {
        System.out.println("Gradient norm at result point: " +
                           function.gradientValueAt(result).getNorm());
    }
}
