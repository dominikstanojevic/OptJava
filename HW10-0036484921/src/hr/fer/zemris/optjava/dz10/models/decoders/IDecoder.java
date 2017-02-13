package hr.fer.zemris.optjava.dz10.models.decoders;

import hr.fer.zemris.optjava.dz10.models.solutions.AbstractSolution;

/**
 * Created by Dominik on 18.10.2016..
 */
public interface IDecoder<T extends AbstractSolution> {
    double[] decode(T value);

    void decode(T value, double[] array);
}
