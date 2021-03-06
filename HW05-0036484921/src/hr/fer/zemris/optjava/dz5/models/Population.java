package hr.fer.zemris.optjava.dz5.models;

import hr.fer.zemris.optjava.dz5.models.solutions.AbstractSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by Dominik on 25.10.2016..
 */
public class Population<T extends AbstractSolution> implements Iterable<T> {
    protected int capacity;
    protected List<T> population = new ArrayList<>();

    public Population() {
        this(Integer.MAX_VALUE);
    }

    public Population(int capacity) {
        this.capacity = capacity;
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
            throw new IndexOutOfBoundsException("Population already at its maximum capacity.");
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
        Collections.sort(population, Collections.reverseOrder());
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

    public T remove(int index) {
        return population.remove(index);
    }

    public Population<T> getSubPopulation(int fromIndex, int toIndex) {
        int capacity = toIndex - fromIndex;
        Population<T> sub = new Population<>(capacity);

        sub.population = new ArrayList<>(population.subList(fromIndex, toIndex));
        return sub;
    }

    public void addPopulation(Population<T> population) {
        this.population.addAll(population.population);
    }
}
