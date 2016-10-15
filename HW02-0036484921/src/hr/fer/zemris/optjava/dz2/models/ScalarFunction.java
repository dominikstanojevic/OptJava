package hr.fer.zemris.optjava.dz2.models;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.Function;

/**
 * Created by Dominik on 15.10.2016..
 */
public class ScalarFunction implements IHFunction {
    private int numberOfVariables;
    private Function<RealVector, Double> valueAt;
    private Function<RealVector, RealVector> gradientAt;
    private Function<RealVector, RealMatrix> hessianAt;

    public ScalarFunction(
            int numberOfVariables, Function<RealVector, Double> valueAt,
            Function<RealVector, RealVector> gradientAt,
            Function<RealVector, RealMatrix> hessianAt) {
        this.numberOfVariables = numberOfVariables;
        this.valueAt = valueAt;
        this.gradientAt = gradientAt;
        this.hessianAt = hessianAt;
    }

    @Override
    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    @Override
    public double valueAt(RealVector v) {
        checkVector(v);
        return valueAt.apply(v);
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
        return hessianAt.apply(v);
    }
}
