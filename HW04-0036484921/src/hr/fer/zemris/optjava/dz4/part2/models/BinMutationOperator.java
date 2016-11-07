package hr.fer.zemris.optjava.dz4.part2.models;

import hr.fer.zemris.optjava.dz4.models.mutations.IMutationOperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Dominik on 6.11.2016..
 */
public class BinMutationOperator implements IMutationOperator<BinContainer> {
    private double mutationRate;

    public BinMutationOperator(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public void mutate(BinContainer solution, Random random) {
        List<Stick> unassigned = destroyBins(solution, random);

        solution.addStick(solution.rearrange(unassigned));

        int n = solution.numberOfSticks();
    }

    private List<Stick> destroyBins(BinContainer container, Random random) {
        List<Stick> unassigned = new ArrayList<>();
        List<Bin> bins = container.chromosome;

        Iterator<Bin> iterator = bins.iterator();
        while (iterator.hasNext()) {
            Bin bin = iterator.next();
            if (random.nextDouble() <= mutationRate) {
                bin.forEach(s -> unassigned.add(s));
                iterator.remove();
            }
        }

        return unassigned;
    }
}
