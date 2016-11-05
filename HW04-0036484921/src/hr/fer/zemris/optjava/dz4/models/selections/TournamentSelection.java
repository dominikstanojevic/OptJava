package hr.fer.zemris.optjava.dz4.models.selections;

import hr.fer.zemris.optjava.dz4.models.Population;
import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;
import hr.fer.zemris.optjava.dz4.models.solutions.DoubleArraySolution;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Dominik on 4.11.2016..
 */
public class TournamentSelection<T extends AbstractSolution> implements ISelection<T> {
    int size;

    public TournamentSelection(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimal tournament size is 2. Given: " + size);
        }

        this.size = size;
    }

    @Override
    public T select(
            Population<T> population, Random random) {

        Population<T> tournament = new Population<>(population.size());

        int popSize = population.size();
        Set<Integer> choosen = new HashSet<>();

        int i = size;
        while(i > 0) {
            int rand = random.nextInt(popSize);

            if(choosen.contains(rand)) {
                continue;
            }

            tournament.addSolution(population.get(rand));
            choosen.add(rand);
            i--;
        }

        return tournament.getBest();
    }
}
