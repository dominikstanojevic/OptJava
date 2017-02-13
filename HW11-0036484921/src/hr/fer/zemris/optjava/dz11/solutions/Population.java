package hr.fer.zemris.optjava.dz11.solutions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dominik on 22.1.2017..
 */
public class Population<T extends GASolution<?>> implements Iterable<T>{
    private List<T> population = new ArrayList<>();
    private int capacity;

    public Population(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Population cannot have capacity less than 1. Given: " + capacity + ".");
        }

        this.capacity = capacity;
    }

    public void add(T solution) {
        Objects.requireNonNull(solution, "Cannot add null value to population");
        if (size() == capacity) {
            throw new IllegalStateException("Capacity reached.");
        }

        population.add(solution);
    }

    public void rank() {
        sort(Comparator.naturalOrder());
    }

    public void sort(Comparator<T> comparator) {
        population.sort(comparator);
    }

    public int size() {
        return population.size();
    }

    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException(
                    "Index is not from valid range. Given " + index + ", valid range is " + "from 0 to " +
                    (size() - 1) + ".");
        }

        return population.get(index);
    }

    public T getBest() {
        rank();
        return population.get(0);
    }

    @Override
    public Iterator<T> iterator() {
        return population.iterator();
    }
}
