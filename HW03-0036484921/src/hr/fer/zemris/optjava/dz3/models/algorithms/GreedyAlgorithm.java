package hr.fer.zemris.optjava.dz3.models.algorithms;

import hr.fer.zemris.optjava.dz3.models.IFunction;
import hr.fer.zemris.optjava.dz3.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz3.models.neighborhoods.INeighborhood;
import hr.fer.zemris.optjava.dz3.models.solutions.AbstractSolution;

/**
 * Created by Dominik on 19.10.2016..
 */
public class GreedyAlgorithm<T extends AbstractSolution> implements IOptAlgorithm<T> {
    private static final int ITERATIONS = 100_000;
    private static final double THRESHOLD = 1e-6;

    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private boolean minimize;

    public GreedyAlgorithm(
            IDecoder<T> decoder, INeighborhood<T> neighborhood, T startWith,
            IFunction function, boolean minimize) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.minimize = minimize;
    }

    @Override
    public T run() {
        int iteration = 0;

        T candidate = startWith;
        candidate.fitness = calculateFitness(candidate);

        while (!stopCondition(candidate, iteration)) {
            T neighbor = neighborhood.randomNeighbor(candidate);
            neighbor.fitness = calculateFitness(neighbor);

            candidate = chooseCandidate(candidate, neighbor);
            System.out.println("Iteration: " + iteration + ", candidate: " + candidate);
            iteration++;
        }

        return candidate;
    }

    private double calculateFitness(T candidate) {
        double result = function.valueAt(decoder.decode(candidate));

        return minimize ? -result : result;
    }

    private T chooseCandidate(T first, T second) {
        int result = first.compareTo(second);

        return result == 1 ? first : second;
    }

    private boolean stopCondition(T candidate, int iteration) {
        if(iteration >= ITERATIONS) return true;

        double fitness = candidate.fitness;
        if(!minimize) {
            fitness = 1 / fitness;
        }

        if(Math.abs(fitness) < THRESHOLD) {
            return true;
        }

        return false;
    }

}
