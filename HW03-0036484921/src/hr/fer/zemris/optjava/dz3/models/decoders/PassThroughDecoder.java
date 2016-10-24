package hr.fer.zemris.optjava.dz3.models.decoders;

import hr.fer.zemris.optjava.dz3.models.solutions.DoubleArraySolution;

import java.util.Arrays;

/**
 * Created by Dominik on 18.10.2016..
 */
public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {

    @Override
    public double[] decode(DoubleArraySolution value) {
        return value.getValue();
    }

    @Override
    public void decode(DoubleArraySolution value, double[] array) {
        double[] values = value.getValue();
        System.arraycopy(values, 0, array, 0, values.length);
    }
}
