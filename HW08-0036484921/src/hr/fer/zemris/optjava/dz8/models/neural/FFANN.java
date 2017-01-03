package hr.fer.zemris.optjava.dz8.models.neural;

import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

/**
 * Created by Dominik on 19.12.2016..
 */
public class FFANN extends AbstractANN {

    public FFANN(int[] numberOfNeurons, ActivationFunction[] functions) {
        layers = new Layer[numberOfNeurons.length];
        layers[0] = new Layer(numberOfNeurons[0]);

        for(int i = 1; i < numberOfNeurons.length; i++) {
            layers[i] = new Layer(numberOfNeurons[i], functions[i -1]);

            layers[i - 1].setNext(layers[i]);
        }

        calculateParameters();
    }

    @Override
    protected void setParameters(RealVector parameters) {
        double[] params = parameters.toArray();
        int startPosition = 0;

        for (int i = 0, n = layers.length - 1; i < n; i++) {
            int numberOfWeights = layers[i].numberOfWeights();

            double[] weightsForLayer = Arrays.copyOfRange(params, startPosition, startPosition + numberOfWeights);
            startPosition += numberOfWeights;

            layers[i].setWeightMatrix(weightsForLayer);
        }
    }
}
