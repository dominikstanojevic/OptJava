package hr.fer.zemris.optjava.dz3.models.decoders;

import hr.fer.zemris.optjava.dz3.models.solutions.BitVectorSolution;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by Dominik on 18.10.2016..
 */
public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution> {
    protected double[] mins;
    protected double[] maxs;
    protected int[] bits;
    protected int n;
    protected int totalBits;

    public BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        this.mins = mins;
        this.maxs = maxs;
        this.bits = bits;
        this.n = n;

        totalBits = Arrays.stream(bits).sum();
    }

    public BitVectorDecoder(double min, double max, int bit, int n) {
        mins = new double[n];
        maxs = new double[n];
        bits = new int[n];
        this.n = n;

        Arrays.fill(mins, min);
        Arrays.fill(maxs, max);
        Arrays.fill(bits, bit);
    }

    @Override
    public double[] decode(BitVectorSolution value) {
        double[] values = new double[n];
        decode(value, values);

        return values;
    }

    protected static long convertNormalToDecimal(byte[] bytes) {
        long result = 0;
        for(int i = 0; i < bytes.length; i++) {
            result += (bytes[i] & 0xFF) << (i * 8);
        }

        return result;
    }

    protected static long convertNormalToDecimal(BitSet bits, int len) {
        return convertNormalToDecimal(bits.toByteArray());
    }

    protected void decode(
            BitVectorSolution solution, double[] array, BiFunction<BitSet, Integer, Long> convertFunction) {
        BitSet bits = solution.getValue();

        for (int i = 0, pos = 0; i < n; i++) {
            long integer = convertFunction.apply(bits.get(pos, pos + this.bits[i]), this.bits[i]);
            array[i] = integer / (Math.pow(2, this.bits[i]) - 1) * (maxs[i] - mins[i]) + mins[i];

            pos += this.bits[i];
        }
    }
}
