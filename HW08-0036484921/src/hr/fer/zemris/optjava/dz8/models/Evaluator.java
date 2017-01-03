package hr.fer.zemris.optjava.dz8.models;

import hr.fer.zemris.optjava.dz8.models.neural.AbstractANN;

import java.util.function.Supplier;

/**
 * Created by Dominik on 18.12.2016..
 */
public class Evaluator {
    private ThreadLocal<AbstractANN> network;
    private Dataset dataset;

    public Evaluator(Supplier<AbstractANN> networkSupplier, Dataset dataset) {
        network = ThreadLocal.withInitial(networkSupplier);
        this.dataset = dataset;
    }

    public double evaluate(Solution solution) {
        return 1. / network.get().evaluate(dataset, solution.values);
    }

    public int numberOfParameters() {
        return network.get().getNumberOfParameters();
    }
}
