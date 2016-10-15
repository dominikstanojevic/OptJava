package hr.fer.zemris.optjava.dz2.models;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 15.10.2016..
 */
public class CompositeFunction implements IHFunction {
    private int numberOfVariables;
    private List<IHFunction> functions = new ArrayList<>();

    public CompositeFunction(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    @Override
    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    @Override
    public double valueAt(RealVector v) {
        double value = 0;
        for (IHFunction function : functions) {
            value += function.valueAt(v);
        }

        return value;
    }

    @Override
    public RealVector gradientValueAt(RealVector v) {
        RealVector vector = new ArrayRealVector(numberOfVariables);
        for (IHFunction function : functions) {
            vector = vector.add(function.gradientValueAt(v));
        }

        return vector;
    }

    @Override
    public RealMatrix getHessianMatrixAt(RealVector v) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(numberOfVariables, numberOfVariables);
        for(IHFunction function : functions) {
            matrix = matrix.add(function.getHessianMatrixAt(v));
        }

        return matrix;
    }

    public void addFunction(IHFunction function) {
        if (function.getNumberOfVariables() != numberOfVariables) {
            throw new IllegalArgumentException(
                    "Illegal number of variables. Expected: " + numberOfVariables +
                    ", given: " + function.getNumberOfVariables());
        }

        functions.add(function);
    }
}
