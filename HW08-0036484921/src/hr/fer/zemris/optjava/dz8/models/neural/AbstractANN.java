package hr.fer.zemris.optjava.dz8.models.neural;

import hr.fer.zemris.optjava.dz8.models.Dataset;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

/**
 * Created by Dominik on 18.12.2016..
 */
public abstract class AbstractANN {
    protected Layer[] layers;
    protected int numberOfParameters;

    public int getNumberOfParameters() {
        return numberOfParameters;
    }

    public RealVector calculateOutput(RealVector input) {
        layers[0].giveInput(input);

        RealVector output = null;
        for (Layer layer : layers) {
            output = layer.calculateOutput();
        }

        return output;
    }

    protected void calculateParameters() {
        for (Layer layer : layers) {
            numberOfParameters += layer.numberOfWeights();

            if (layer.isContextActivated()) {
                numberOfParameters += layer.numberOfNeurons();
            }
        }
    }

    protected abstract void setParameters(RealVector parameters);

    public double evaluate(Dataset dataset, RealVector parameters) {
        setParameters(parameters);

        int n = dataset.input.getRowDimension();
        double total = 0;

        for (int i = 0; i < n; i++) {
            RealVector input = dataset.input.getRowVector(i);
            double output = dataset.output.getEntry(i);

            double result = calculateOutput(input).getEntry(0);
            double diff = result - output;

            total += diff * diff;
        }

        return total / n;
    }

    public int numberOfInputNeurons() {
        return layers[0].numberOfNeurons();
    }
}
