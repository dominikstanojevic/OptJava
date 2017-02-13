package hr.fer.zemris.optjava.dz4.models.selections;

import hr.fer.zemris.optjava.dz9.models.Population;
import hr.fer.zemris.optjava.dz9.models.solutions.AbstractSolution;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by Dominik on 26.10.2016..
 */
public class RouletteWheelSelection<T extends AbstractSolution> implements ISelection<T> {
    @Override
    public T selectBest(Population<T> population, Random random) {
        AbstractSolution worst = population.getWorst();
        return select(population, random, s -> s.fitness - worst.fitness);
    }

    @Override
    public T selectWorst(Population<T> population, Random random) {
        AbstractSolution best = population.getBest();
        return select(population, random, s -> -(s.fitness - best.fitness));
    }

    private T select(Population<T> population, Random random, Function<T, Double> fitness) {
        Map<T, Double> relative = new LinkedHashMap<>();
        double total = 0;
        for (T s : population) {
            double r = fitness.apply(s);

            total += r;
            relative.put(s, r);
        }

        Map<T, Double> accumulated = new LinkedHashMap<>();
        double previous = 0;
        for (Map.Entry<T, Double> s : relative.entrySet()) {
            double r = s.getValue() / total + previous;
            accumulated.put(s.getKey(), r);

            previous = r;
        }

        double rand = random.nextDouble();
        for (Map.Entry<T, Double> s : accumulated.entrySet()) {
            if (s.getValue() > rand) {
                return s.getKey();
            }
        }

        throw new RuntimeException("Need to choose one solution.");
    }
}
