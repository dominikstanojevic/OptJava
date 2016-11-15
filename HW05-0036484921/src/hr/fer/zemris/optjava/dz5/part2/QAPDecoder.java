package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.models.IDecoder;

/**
 * Created by Dominik on 12.11.2016..
 */
public class QAPDecoder implements IDecoder<QAPSolution> {
    @Override
    public double[] decode(QAPSolution value) {
        double[] array = new double[value.chromosome.length];
        decode(value, array);

        return array;
    }

    @Override
    public void decode(QAPSolution value, double[] array) {
        int[] chromosome = value.chromosome;

        for (int i = 0; i < chromosome.length; i++) {
            array[i] = chromosome[i];
        }
    }
}
