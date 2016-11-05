package hr.fer.zemris.optjava.dz4.models.selections;

import hr.fer.zemris.optjava.dz4.models.Population;
import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;
import hr.fer.zemris.optjava.dz4.models.solutions.DoubleArraySolution;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Dominik on 26.10.2016..
 */
public class RouletteWheelSelection<T extends AbstractSolution> implements ISelection<T> {
    @Override
    public T select(Population<T> population, Random random) {
        AbstractSolution worst = population.getWorst();

        Map<T, Double> relative = new LinkedHashMap<>();
        double total = 0;
        Iterator<T> iterator = population.iterator();
        while (iterator.hasNext()) {
            T s = iterator.next();
            double r = s.fitness - worst.fitness;

            total += r;
            relative.put(s, r);
        }

        Map<T, Double> accumulated = new LinkedHashMap<>();
        double previous = 0;
        for(Map.Entry<T, Double> s : relative.entrySet()) {
            double r = s.getValue() / total + previous;
            accumulated.put(s.getKey(), r);

            previous += r;
        }

        double rand = random.nextDouble();
        for(Map.Entry<T, Double> s : accumulated.entrySet()) {
            if (s.getValue() > rand) {
                return s.getKey();
            }
        }

        throw new RuntimeException("Need to choose one solution.");
    }
}
