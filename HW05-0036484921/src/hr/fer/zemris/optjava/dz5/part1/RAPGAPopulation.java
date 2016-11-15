package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.models.Population;
import hr.fer.zemris.optjava.dz5.models.solutions.AbstractSolution;

import java.util.Random;
import java.util.function.Function;

/**
 * Created by Dominik on 11.11.2016..
 */
public class RAPGAPopulation<T extends AbstractSolution> extends Population<T> {
    public RAPGAPopulation(int maxPop) {
        super(maxPop);
    }



    @Override
    public void fill(Random random, Function<Random, T> solutionSupplier) {
        while (population.size() < capacity) {
            T solution = solutionSupplier.apply(random);
            population.add(solution);
        }
    }

    @Override
    public void addSolution(T solution) {
        if (population.contains(solution)) {
            return;
        }

        super.addSolution(solution);
    }
}
