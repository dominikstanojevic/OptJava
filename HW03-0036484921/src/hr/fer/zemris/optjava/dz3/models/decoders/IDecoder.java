package hr.fer.zemris.optjava.dz3.models.decoders;

/**
 * Created by Dominik on 18.10.2016..
 */
public interface IDecoder<T> {
    double[] decode(T value);

    void decode(T value, double[] array);
}
