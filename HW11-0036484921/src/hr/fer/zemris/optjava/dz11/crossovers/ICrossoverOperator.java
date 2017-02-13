package hr.fer.zemris.optjava.dz11.crossovers;

import hr.fer.zemris.optjava.dz11.solutions.GASolution;
import hr.fer.zemris.optjava.dz11.solutions.Pair;

/**
 * Created by Dominik on 22.1.2017..
 */
public interface ICrossoverOperator<T extends GASolution<?>> {
    Pair<T, T> getChildren(Pair<T, T> parents);
}
