package hr.fer.zemris.optjava.dz11.selections;

import hr.fer.zemris.optjava.dz11.solutions.GASolution;
import hr.fer.zemris.optjava.dz11.solutions.Population;

/**
 * Created by Dominik on 22.1.2017..
 */
public interface ISelectionOperator<T extends GASolution<?>> {
    T selectBest(Population<T> population);
    T selectWorst(Population<T> population);
}
