package hr.fer.zemris.optjava.dz8.models.neural;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 5.12.2016..
 */
public class ContextLayer {
    private RealVector values;
    private RealMatrix weights;

    public ContextLayer(int size) {
        this.values = new ArrayRealVector(size);
        this.weights = Utils.createRandomMatrix(size, size, -1, 1);
    }

    void copyValues(RealVector values) {
        this.values = new ArrayRealVector(values);
    }

    RealVector getValues() {
        int n = values.getDimension();
        double[] result = new double[n];

        for(int i = 0; i < n; i++) {
            RealVector column = weights.getColumnVector(i);
            result[i] = values.dotProduct(column);
        }

        return new ArrayRealVector(result);
    }

    public void clear() {
        values = new ArrayRealVector(new double[values.getDimension()]);
    }

    public int numberOfWeights() {
        return weights.getColumnDimension() * weights.getRowDimension();
    }

    public void setWeights(double[] weights) {
        int n = values.getDimension();
        double[][] data = new double[n][n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                data[i][j] = weights[i * n + j];
            }
        }

        this.weights = MatrixUtils.createRealMatrix(data);
    }

    public RealMatrix getWeights() {
        return weights;
    }

    public void setValues(double[] values) {
        this.values = new ArrayRealVector(values);
    }
}
