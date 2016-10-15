package hr.fer.zemris.optjava.dz2.models;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.Function;

/**
 * Created by Dominik on 15.10.2016..
 */
public class ScalarFunction implements IHFunction {
    private int numberOfVariables;
    private Function<RealVector, Double> vauleAt;
    private Function<RealVector, RealVector> gradientAt;
    private Function<RealVector, RealMatrix> hessianAt;

    public ScalarFunction(
            int numberOfVariables, Function<RealVector, Double> vauleAt,
            Function<RealVector, RealVector> gradientAt,
            Function<RealVector, RealMatrix> hessianAt) {
        this.numberOfVariables = numberOfVariables;
        this.vauleAt = vauleAt;
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
        return vauleAt.apply()
    }

    @Override
    public RealVector gradientValueAt(RealVector v) {
        return null;
    }

    @Override
    public RealMatrix getHessianMatrixAt(
            RealVector v) {
        return null;
    }
}
