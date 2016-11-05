package hr.fer.zemris.optjava.dz4.part2.models;

import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public class BinContainer extends AbstractSolution<List<Bin>> {
    protected BinContainer(List<Bin> chromosome) {
        super(chromosome);
    }

    public BinContainer duplicate() {
        return new BinContainer(new ArrayList<>(chromosome));
    }

    public int size() {
        return chromosome.size();
    }

    @Override
    public void randomize(Random random) {
        throw new UnsupportedOperationException("Cannot randomize bin container!");
    }

    @Override
    public String toString() {
        return null;
    }

}
