package hr.fer.zemris.optjava.dz11.selections;

import hr.fer.zemris.optjava.dz11.rng.RNG;
import hr.fer.zemris.optjava.dz11.solutions.GASolution;
import hr.fer.zemris.optjava.dz11.solutions.Population;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Dominik on 22.1.2017..
 */
public class TournamentSelection<T extends GASolution<?>> implements ISelectionOperator<T> {
    private int size;

    public TournamentSelection(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Size of the tournament cannot be less than 2. Given: " + size + ".");
        }

        this.size = size;
    }

    @Override
    public T selectBest(Population<T> population) {
        return select(population, Comparator.naturalOrder());
    }

    @Override
    public T selectWorst(Population<T> population) {
        return select(population, Comparator.reverseOrder());
    }

    private T select(Population<T> population, Comparator<T> comparator) {
        int populationSize = population.size();
        if(populationSize < size) {
            throw new IllegalStateException("Population is less than tournament size.");
        }

        Set<T> set = new HashSet<>();

        while(set.size() < size) {
            T solution = population.get(RNG.getRNG().nextInt(0, populationSize));
            set.add(solution);
        }

        List<T> tournament = new ArrayList<>(set);
        tournament.sort(comparator);

        return tournament.get(0);
    }
}
