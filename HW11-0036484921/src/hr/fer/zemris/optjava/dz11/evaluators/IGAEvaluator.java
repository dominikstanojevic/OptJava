package hr.fer.zemris.optjava.dz11.evaluators;

import hr.fer.zemris.optjava.dz11.solutions.GASolution;

/**
 * Created by Dominik on 20.1.2017..
 */
public interface IGAEvaluator<T extends GASolution<?>> {
    void evaluate(T solution);
}
