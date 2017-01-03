package hr.fer.zemris.optjava.dz9.models.decoders;

import hr.fer.zemris.optjava.dz9.models.solutions.DoubleArraySolution;

/**
 * Created by Dominik on 18.10.2016..
 */
public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {

    @Override
    public double[] decode(DoubleArraySolution value) {
        return value.chromosome;
    }

    @Override
    public void decode(DoubleArraySolution value, double[] array) {
        double[] values = value.chromosome;
        System.arraycopy(values, 0, array, 0, values.length);
    }
}
