package hr.fer.zemris.optjava.dz10.models;

import hr.fer.zemris.optjava.dz10.models.solutions.AbstractSolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by Dominik on 25.10.2016..
 */
public class Population<T extends AbstractSolution> implements Iterable<T> {
    private int capacity;
    private List<T> population;

    public Population(int capacity) {
        this.capacity = capacity;
        this.population = new ArrayList<>(capacity);
    }

    public Population(Population<T> population) {
        capacity = population.capacity;
        this.population = new ArrayList<>(population.population);
    }

    public Population(Collection<T> population) {
        this.capacity = population.size();
        this.population = new ArrayList<>(population);
    }



    public void fill(Random random, Function<Random, T> solutionSupplier) {
        for (int i = 0; i < capacity; i++) {
            T solution = solutionSupplier.apply(random);
            population.add(solution);
        }
    }

    public int size() {
        return population.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public void addSolution(T solution) {
        if (population.size() >= capacity) {
            return;
        }

        population.add(solution);
    }

    public void removeSolution(T solution) {
        population.remove(solution);
    }

    @Override
    public Iterator<T> iterator() {
        return population.iterator();
    }

    public void sort() {
        population.sort(Collections.reverseOrder());
    }

    public T getBest() {
        sort();
        return population.get(0);
    }

    public T getWorst() {
        sort();
        return population.get(population.size() - 1);
    }

    public T get(int index) {
        return population.get(index);
    }

    public void removeAll(Population<T> population) {
        this.population.removeAll(population.population);
    }

    public void clear() {
        population.clear();
    }

    public static <T extends AbstractSolution> Population<T> mergePopulations(
            Population<T> first, Population<T> second) {
        Population<T> newPop = new Population<>(first.capacity + second.capacity);
        newPop.population.addAll(first.population);
        newPop.population.addAll(second.population);

        return newPop;
    }

    public void addAll(Population<T> population) {
        int n = Math.min(population.size(), this.capacity - this.size());

        for(int i = 0; i < n; i++) {
            this.population.add(population.population.get(i));
        }
    }

    public Stream<T> stream() {
        return population.stream();
    }

    public void sort(Comparator<T> comparator) {
        population.sort(comparator);
    }
}
