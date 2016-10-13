package hr.fer.zemris.optjava.dz2.assignments;

import hr.fer.zemris.optjava.dz2.algorithms.NumOptAlgorithms;
import hr.fer.zemris.optjava.dz2.models.IHFunction;
import hr.fer.zemris.optjava.dz2.models.TwoVariableFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * Created by Dominik on 13.10.2016..
 */
public class FirstFunction {
	public static void main(String[] args) {
		BinaryOperator<Double> f =
				(first, second) -> first * first - Math.pow((second - 1), 2);
		BiFunction<Double, Double, RealVector> gradient = (first, second) -> {
			double firstResult = 2 * first;
			double secondResult = 2 * (second - 1);

			return new ArrayRealVector(new double[] { firstResult, secondResult });
		};
		BiFunction<Double, Double, RealMatrix> hessian = (first, second) -> MatrixUtils
				.createRealMatrix(new double[][] { { 2, 0 }, { 0, 2 } });

		IHFunction function = new TwoVariableFunction(f, gradient, hessian);
		NumOptAlgorithms.gradientDescentAlgorithm(function, 100_000);
		System.out.println("----------------------------------------------------");
		NumOptAlgorithms.newtonAlgorithm(function, 100_000);
	}
}
