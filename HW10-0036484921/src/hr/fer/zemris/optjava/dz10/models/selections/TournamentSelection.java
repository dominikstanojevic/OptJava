package hr.fer.zemris.optjava.dz10.models.selections;

import hr.fer.zemris.optjava.dz10.models.Population;
import hr.fer.zemris.optjava.dz10.models.solutions.AbstractSolution;

import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Dominik on 3.1.2017..
 */
public class TournamentSelection<T extends AbstractSolution<?>> implements ISelection<T> {
    private int size;

    public TournamentSelection(int size) {
        this.size = size;
    }

    private T select(
            Population<T> population, Random random, Comparator<T> comparator) {
        TreeSet<T> tournament = new TreeSet<>(comparator);

        int length = population.size();
        while (tournament.size() < size) {
            T solution = population.get(random.nextInt(length));
            tournament.add(solution);
        }

        return tournament.first();
    }

    @Override
    public T selectBest(Population<T> population, Random random) {
        return select(population, random, Comparator.reverseOrder());
    }

    @Override
    public T selectWorst(Population<T> population, Random random) {
        return select(population, random, Comparator.naturalOrder());
    }
}
