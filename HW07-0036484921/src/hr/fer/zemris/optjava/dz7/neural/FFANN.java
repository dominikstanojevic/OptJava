package hr.fer.zemris.optjava.dz7.neural;

import hr.fer.zemris.optjava.dz7.Dataset;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

/**
 * Created by Dominik on 7.12.2016..
 */
public class FFANN {
    private Layer[] layers;
    private Dataset dataset;
    private int numberOfWeights;

    public FFANN(int[] numberOfNeurons, ActivationFunction[] functions, Dataset dataset) {
        layers = new Layer[numberOfNeurons.length];

        layers[0] = new Layer(numberOfNeurons[0]);
        for (int i = 1; i < numberOfNeurons.length; i++) {
            layers[i] = new Layer(numberOfNeurons[i], functions[i - 1]);
            layers[i - 1].setNext(layers[i]);
            layers[i].setPrevious(layers[i - 1]);
        }

        this.dataset = dataset;
        calculateWeights();
    }

    private void calculateWeights() {
        for (Layer layer : layers) {
            numberOfWeights += layer.numberOfWeights();
        }
    }

    public double evaluate(double[] weights) {
        distributeWeights(weights);

        RealMatrix outputs = calculateOutputs();

        return calculateCost(outputs);
    }

    private double calculateCost(RealMatrix outputs) {
        double total = 0;
        for (int i = 0; i < dataset.numberOfSamples; i++) {
            RealVector output = outputs.getRowVector(i);
            RealVector expected = dataset.outputs.getRowVector(i);

            RealVector difference = output.subtract(expected);
            total += difference.getL1Norm();
        }

        return total / dataset.numberOfSamples;
    }

    private RealMatrix calculateOutputs() {

        double[][] outputs = new double[dataset.numberOfSamples][dataset.outputs.getColumnDimension()];

        for (int i = 0; i < dataset.numberOfSamples; i++) {
            outputs[i] = calculateOutput(dataset.inputs.getRow(i));
        }

        return MatrixUtils.createRealMatrix(outputs);
    }

    private double[] calculateOutput(double[] data) {
        layers[0].giveInput(new ArrayRealVector(data));

        RealVector output = null;
        for (Layer layer : layers) {
            output = layer.calculateOutput();
        }

        return output.toArray();
    }

    private void distributeWeights(double[] weights) {
        int startPosition = 0;

        for (int i = 0, n = layers.length - 1; i < n; i++) {
            int numberOfWeights = layers[i].numberOfWeights();

            double[] weightsForLayer = Arrays.copyOfRange(weights, startPosition, numberOfWeights);
            startPosition += numberOfWeights;

            layers[i].setWeightMatrix(weightsForLayer);
        }
    }

}
