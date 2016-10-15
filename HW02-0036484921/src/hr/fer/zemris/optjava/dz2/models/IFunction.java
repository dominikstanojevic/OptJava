package hr.fer.zemris.optjava.dz2.models;

import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;

/**
 * Created by Dominik on 12.10.2016..
 */
public interface IFunction {
    int getNumberOfVariables();

    double valueAt(RealVector v);

    RealVector gradientValueAt(RealVector v);

    default void checkVector(RealVector v) {
        Objects.requireNonNull(v, "Vector cannot be null");
        if (v.getDimension() != getNumberOfVariables()) {
            throw new IllegalArgumentException(
                    "Expect 2-dimensional vector, got: " + v.getDimension());
        }
    }
}
