package hr.fer.zemris.optjava.dz2.models;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 17.10.2016..
 */
public class SquareFunction implements IHFunction {
    private IHFunction function;
    private RealVector delta = new ArrayRealVector(6, 1e-2);

    public SquareFunction(IHFunction function) {
        this.function = function;
    }

    @Override
    public int getNumberOfVariables() {
        return function.getNumberOfVariables();
    }

    @Override
    public double valueAt(RealVector v) {
        double value = function.valueAt(v);
        return Math.pow(value, 2);
    }

    @Override
    public RealVector gradientValueAt(RealVector v) {
        return function.gradientValueAt(v).mapMultiply(2 * function.valueAt(v));
    }

    @Override
    public RealMatrix getHessianMatrixAt(
            RealVector v) {
        RealMatrix column =
                MatrixUtils.createColumnRealMatrix(function.gradientValueAt(v).toArray());
        RealMatrix row = column.transpose();

        return column.multiply(row).scalarMultiply(2);
    }
}
