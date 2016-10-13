package hr.fer.zemris.optjava.dz2.models;

import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 12.10.2016..
 */
public interface IFunction {
    int getNumberOfVariables();
    double valueAt(RealVector v);
    RealVector
    gradientValueAt(RealVector v);
}
