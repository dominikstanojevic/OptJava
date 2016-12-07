package hr.fer.zemris.optjava.dz7;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 7.12.2016..
 */
public class Dataset {
    public RealMatrix inputs;
    public RealMatrix outputs;

    public int numberOfSamples;

    public Dataset(RealMatrix inputs, RealMatrix outputs) {
        this.inputs = inputs;
        this.outputs = outputs;

        numberOfSamples = inputs.getRowDimension();
    }

    public Dataset(double[][] inputs, double[][] outputs) {
        this.inputs = MatrixUtils.createRealMatrix(inputs);
        this.outputs = MatrixUtils.createRealMatrix(outputs);

        numberOfSamples = this.inputs.getRowDimension();
    }
}
