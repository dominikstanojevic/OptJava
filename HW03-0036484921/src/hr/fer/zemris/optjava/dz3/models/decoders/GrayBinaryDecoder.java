package hr.fer.zemris.optjava.dz3.models.decoders;

import hr.fer.zemris.optjava.dz3.models.solutions.BitVectorSolution;

import java.util.BitSet;

/**
 * Created by Dominik on 19.10.2016..
 */
public class GrayBinaryDecoder extends BitVectorDecoder {
    public GrayBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public GrayBinaryDecoder(double min, double max, int bit, int n) {
        super(min, max, bit, n);
    }

    @Override
    public void decode(BitVectorSolution value, double[] array) {
        decode(value, array, (bits, len) -> convertFromGrayToDecimal(bits, len));
    }

    private static long convertFromGrayToDecimal(BitSet bits, int len) {
        BitSet bs = new BitSet(len);

        int pos = len - 1;
        bs.set(pos, bits.get(pos));
        pos--;

        while (pos >= 0) {
            bs.set(pos, bs.get(pos + 1) ^ bits.get(pos));
            pos--;
        }

        return convertNormalToDecimal(bs, len);
    }
}
