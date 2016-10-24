package hr.fer.zemris.optjava.dz3.models.decoders;

import hr.fer.zemris.optjava.dz3.models.solutions.BitVectorSolution;

import java.util.BitSet;

/**
 * Created by Dominik on 18.10.2016..
 */
public class NaturalBinaryDecoder extends BitVectorDecoder {
    public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public NaturalBinaryDecoder(double min, double max, int bit, int n) {
        super(min, max, bit, n);
    }

    @Override
    public void decode(BitVectorSolution value, double[] array) {
        decode(value, array, (bits, len) -> convertNormalToDecimal(bits, len));
    }
}
