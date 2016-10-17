package hr.fer.zemris.optjava.dz2.assignments;

import hr.fer.zemris.optjava.dz2.algorithms.NumOptAlgorithms;
import hr.fer.zemris.optjava.dz2.models.IHFunction;
import hr.fer.zemris.optjava.dz2.models.ScalarFunction;
import hr.fer.zemris.optjava.dz2.models.TwoVariableFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Created by Dominik on 13.10.2016..
 */
public class SecondFunction {
    public static void main(String[] args) {
        Function<RealVector, Double> f = vector -> Math.pow(vector.getEntry(0) - 1, 2) +
                                                   10 * Math.pow(vector.getEntry(1) - 2, 2);
        Function<RealVector, RealVector> gradient = vector -> {
            double firstResult = 2 * (vector.getEntry(0) - 1);
            double secondResult = 20 * (vector.getEntry(1) - 2);

            return new ArrayRealVector(new double[] { firstResult, secondResult });
        };
        Function<RealVector, RealMatrix> hessian =
                vector -> MatrixUtils.createRealMatrix(new double[][] { { 2, 0 }, { 0, 20 } });

        IHFunction function = new ScalarFunction(2, f, gradient, hessian);
        RealVector result;
        result = NumOptAlgorithms
                .gradientDescentAlgorithm(function, 1_000, function.getNumberOfVariables());
        FirstFunction.printGradientValue(function, result);
        System.out.println("-----------------------------------------------");
        result = NumOptAlgorithms
                .newtonAlgorithm(function, 1_000, function.getNumberOfVariables());
        FirstFunction.printGradientValue(function, result);
    }
}
