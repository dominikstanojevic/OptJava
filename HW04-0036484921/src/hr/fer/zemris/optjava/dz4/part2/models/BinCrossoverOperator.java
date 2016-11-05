package hr.fer.zemris.optjava.dz4.part2.models;

import hr.fer.zemris.optjava.dz4.models.Pair;
import hr.fer.zemris.optjava.dz4.models.crossovers.ICrossoverOperator;

import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public class BinCrossoverOperator implements ICrossoverOperator<BinContainer> {

    @Override
    public Pair<BinContainer> getChildren(
            Pair<BinContainer> parents, Random random) {
        return null;
    }
}
