package hr.fer.zemris.optjava.dz4.part2.models;

import hr.fer.zemris.optjava.dz4.models.decoders.IDecoder;

import java.util.List;

/**
 * Created by Dominik on 7.11.2016..
 */
public class BinContainerDecoder implements IDecoder<BinContainer> {
    @Override
    public double[] decode(BinContainer value) {
        double[] array = new double[value.chromosome.size()];

        decode(value, array);

        return array;
    }

    @Override
    public void decode(BinContainer value, double[] array) {
        List<Bin> chromosome = value.chromosome;
        int size = chromosome.size();

        for (int i = 0; i < size; i++) {
            Bin bin = chromosome.get(i);
            array[i] = ((double) bin.getCurrentHeight()) / Bin.maxHeight;
        }
    }
}
