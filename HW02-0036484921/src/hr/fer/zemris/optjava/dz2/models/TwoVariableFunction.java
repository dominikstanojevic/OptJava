package hr.fer.zemris.optjava.dz2.models;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * Created by Dominik on 13.10.2016..
 */
public class TwoVariableFunction implements IHFunction {
	public static final int NUMBER_OF_VARIABLES = 2;

	private BinaryOperator<Double> valueAt;
	private BiFunction<Double, Double, RealVector> gradientValueAt;
	private BiFunction<Double, Double, RealMatrix> hessianMatrix;

	public TwoVariableFunction(
			BinaryOperator<Double> valueAt,
			BiFunction<Double, Double, RealVector> gradientValueAt,
			BiFunction<Double, Double, RealMatrix> hessianMatrix) {
		this.valueAt = valueAt;
		this.gradientValueAt = gradientValueAt;
		this.hessianMatrix = hessianMatrix;
	}

	@Override
	public int getNumberOfVariables() {
		return NUMBER_OF_VARIABLES;
	}

	@Override
	public double valueAt(RealVector v) {
		checkVector(v);

		return valueAt.apply(v.getEntry(0), v.getEntry(1));
	}



	@Override
	public RealVector gradientValueAt(RealVector v) {
		checkVector(v);

		return gradientValueAt.apply(v.getEntry(0), v.getEntry(1));
	}

	@Override
	public RealMatrix getHessianMatrixAt(
			RealVector v) {
		checkVector(v);

		return hessianMatrix.apply(v.getEntry(0), v.getEntry(1));
	}
}
