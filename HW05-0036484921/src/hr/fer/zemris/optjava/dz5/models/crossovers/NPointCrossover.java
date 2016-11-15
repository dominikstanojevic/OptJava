package hr.fer.zemris.optjava.dz5.models.crossovers;

import hr.fer.zemris.optjava.dz5.models.Pair;
import hr.fer.zemris.optjava.dz5.models.solutions.BitVectorSolution;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 11.11.2016..
 */
public class NPointCrossover implements ICrossoverOperator<BitVectorSolution> {
    private int n;
    private Supplier<BitVectorSolution> supplier;

    public NPointCrossover(int n, Supplier<BitVectorSolution> supplier) {
        this.n = n;
        this.supplier = supplier;
    }

    @Override
    public BitVectorSolution getChild(
            Pair<BitVectorSolution, BitVectorSolution> parents, Random random) {
        Set<Integer> points = new HashSet<>();

        int bound = parents.first.chromosome.length;

        while (points.size() < n) {
            points.add(random.nextInt(bound));
        }

        List<Integer> listPoints = points.stream().sorted().collect(Collectors.toList());

        BitVectorSolution child = supplier.get();
        boolean[] array = child.chromosome;
        for (int i = 0, pos = 0; i < n; i++) {
            int point = listPoints.get(i);

            if (i % 2 == 0) {
                while (pos < point) {
                    array[pos] = parents.first.chromosome[pos];
                    pos++;
                }
            } else {
                while (pos < point) {
                    array[pos] = parents.second.chromosome[pos];
                    pos++;
                }
            }
        }

        return child;
    }
}
