package hr.fer.zemris.optjava.dz5.models.solutions;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by Dominik on 11.11.2016..
 */
public class BitVectorSolution extends AbstractSolution<boolean[]> {
    private Integer hashCode;

    public BitVectorSolution(int size) {
        super(new boolean[size]);
    }

    public BitVectorSolution(boolean[] chromosome) {
        super(chromosome);
    }

    public BitVectorSolution duplicate() {
        BitVectorSolution bvs = new BitVectorSolution(Arrays.copyOf(chromosome, chromosome.length));
        bvs.hashCode = hashCode;

        return bvs;
    }

    @Override
    public void randomize(Random random) {
        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] = random.nextBoolean();
        }

        hashCode();
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (boolean value : chromosome) {
            sj.add(value ? "1" : "0");
        }

        return sj.toString();
    }

    public int cardinality() {
        int n = 0;
        for (boolean bit : chromosome) {
            if (bit) {
                n++;
            }
        }

        return n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BitVectorSolution that = (BitVectorSolution) o;

        if (this.hashCode() != that.hashCode()) {
            return false;
        }

        return Arrays.equals(this.chromosome, that.chromosome);
    }

    @Override
    public int hashCode() {
        if (hashCode == null) {
            hashCode = chromosome.hashCode();
        }

        return hashCode;
    }
}
