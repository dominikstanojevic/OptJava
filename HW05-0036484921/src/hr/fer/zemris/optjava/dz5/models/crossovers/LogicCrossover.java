package hr.fer.zemris.optjava.dz5.models.crossovers;

import hr.fer.zemris.optjava.dz5.models.Pair;
import hr.fer.zemris.optjava.dz5.models.solutions.BitVectorSolution;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Dominik on 11.11.2016..
 */
public abstract class LogicCrossover implements ICrossoverOperator<BitVectorSolution> {
    private Supplier<BitVectorSolution> supplier;
    private BiFunction<Boolean, Boolean, Boolean> function;

    private LogicCrossover(
            Supplier<BitVectorSolution> supplier, BiFunction<Boolean, Boolean, Boolean> function) {
        this.supplier = supplier;
        this.function = function;
    }

    @Override
    public BitVectorSolution getChild(
            Pair<BitVectorSolution, BitVectorSolution> parents, Random random) {
        BitVectorSolution bvs = supplier.get();
        boolean[] chromosome = bvs.chromosome;

        for(int i = 0; i < chromosome.length; i++) {
            chromosome[i] = function.apply(parents.first.chromosome[i], parents.second.chromosome[i]);
        }

        return bvs;
    }

    public static class ANDCrossover extends LogicCrossover {
        public ANDCrossover(
                Supplier<BitVectorSolution> supplier) {
            super(supplier, (f, s) -> f & s);
        }
    }

    public static class ORCrossover extends LogicCrossover {

        public ORCrossover(
                Supplier<BitVectorSolution> supplier) {
            super(supplier, (f, s) -> f | s);
        }
    }

    public static class XORCrossover extends LogicCrossover {

        public XORCrossover(
                Supplier<BitVectorSolution> supplier) {
            super(supplier, (f, s) -> f ^ s);
        }
    }
}
