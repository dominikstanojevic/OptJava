package hr.fer.zemris.optjava.dz2.models;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.Function;

/**
 * Created by Dominik on 13.10.2016..
 */
public class LinearFunction implements IHFunction {
	private int numberOfVariables;
	Function<RealVector, Double> function;
	Function<RealVector, RealVector> gradientAt;
	Function<RealVector, RealMatrix> hessian;

	public LinearFunction(
			int numberOfVariables, Function<RealVector, Double> function,
			Function<RealVector, RealVector> gradientAt,
			Function<RealVector, RealMatrix> hessian) {
		this.numberOfVariables = numberOfVariables;
		this.function = function;
		this.gradientAt = gradientAt;
		this.hessian = hessian;
	}

	@Override
	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public double valueAt(RealVector v) {
		checkVector(v);
		return function.apply(v);
	}

	@Override
	public RealVector gradientValueAt(RealVector v) {
		checkVector(v);
		return gradientAt.apply(v);
	}

	@Override
	public RealMatrix getHessianMatrixAt(
			RealVector v) {
		checkVector(v);
		return hessian.apply(v);
	}
}
