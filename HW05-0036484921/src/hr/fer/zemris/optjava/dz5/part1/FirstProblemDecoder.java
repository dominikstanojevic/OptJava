package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.models.IDecoder;
import hr.fer.zemris.optjava.dz5.models.solutions.BitVectorSolution;

/**
 * Created by Dominik on 11.11.2016..
 */
public class FirstProblemDecoder implements IDecoder<BitVectorSolution> {
    @Override
    public double[] decode(BitVectorSolution value) {
        double[] array = new double[1];
        decode(value, array);

        return array;
    }

    @Override
    public void decode(BitVectorSolution value, double[] array) {
        array[0] = value.cardinality();
    }
}
