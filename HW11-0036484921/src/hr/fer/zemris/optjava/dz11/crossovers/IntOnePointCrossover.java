package hr.fer.zemris.optjava.dz11.crossovers;

import hr.fer.zemris.optjava.dz11.rng.RNG;
import hr.fer.zemris.optjava.dz11.solutions.GASolution;
import hr.fer.zemris.optjava.dz11.solutions.Pair;

/**
 * Created by Dominik on 22.1.2017..
 */
public class IntOnePointCrossover implements ICrossoverOperator<GASolution<int[]>> {
    @Override
    public Pair<GASolution<int[]>, GASolution<int[]>> getChildren(
            Pair<GASolution<int[]>, GASolution<int[]>> parents) {
        int[] firstParent = parents.first.getData();
        int[] secondParent = parents.second.getData();

        if (firstParent.length != secondParent.length) {
            throw new IllegalStateException("First parent has chromosome of different size than second parent");
        }
        int length = firstParent.length;

        GASolution<int[]> firstChild = parents.first.newLikeThis();
        GASolution<int[]> secondChild = parents.second.newLikeThis();

        int[] firstChromosome = firstChild.getData();
        int[] secondChromosome = secondChild.getData();

        int point = RNG.getRNG().nextInt(0, length);

        System.arraycopy(firstParent, 0, firstChromosome, 0, point);
        System.arraycopy(secondParent, 0, secondChromosome, 0, point);

        int diff = length - point;
        System.arraycopy(secondParent, point, firstChromosome, point, diff);
        System.arraycopy(firstParent, point, secondChromosome, point, diff);



        return new Pair<>(firstChild, secondChild);
    }
}
