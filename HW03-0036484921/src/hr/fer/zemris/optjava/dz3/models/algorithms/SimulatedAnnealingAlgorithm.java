package hr.fer.zemris.optjava.dz3.models.algorithms;

import hr.fer.zemris.optjava.dz3.models.IFunction;
import hr.fer.zemris.optjava.dz3.models.RegresijaSustava;
import hr.fer.zemris.optjava.dz3.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz3.models.neighborhoods.INeighborhood;
import hr.fer.zemris.optjava.dz3.models.solutions.AbstractSolution;

import java.util.Random;

/**
 * Created by Dominik on 21.10.2016..
 */
public class SimulatedAnnealingAlgorithm<T extends AbstractSolution> implements IOptAlgorithm<T> {
    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startsWith;
    private IFunction function;
    private boolean minimize;
    private ITempSchedule schedule;
    private Random random;

    public SimulatedAnnealingAlgorithm(
            IDecoder<T> decoder, INeighborhood<T> neighborhood, T startsWith, IFunction function, boolean minimize,
            ITempSchedule schedule, Random random) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startsWith = startsWith;
        this.function = function;
        this.minimize = minimize;
        this.schedule = schedule;
        this.random = random;
    }

    @Override
    public T run() {
        T candidate = startsWith;

        for (int i = 0, outer = schedule.getOuterLoopCounter(); i < outer; i++) {
            double temperature = schedule.getNextTemperature();
            for (int j = 0, inner = schedule.getInnerLoopCounter(); j < inner; j++) {
                T neighbor = neighborhood.randomNeighbor(candidate);
                candidate = chooseCandidate(candidate, neighbor, temperature);
            }

            System.out.println("Candidate: " + candidate + ", outer: " + i + ", error: " +
                               RegresijaSustava.calculateError(candidate));
        }

        return candidate;
    }

    private T chooseCandidate(T candidate, T neighbor, double temperature) {
        candidate.fitness = function.valueAt(decoder.decode(candidate));
        neighbor.fitness = function.valueAt(decoder.decode(neighbor));

        double delta = neighbor.fitness - candidate.fitness * (minimize ? 1 : -1);
        if (delta <= 0) {
            return neighbor;
        }

        return random.nextDouble() <= Math.exp(-delta / temperature) ? neighbor : candidate;
    }

    private double calculateDelta(T neighbor, T candidate) {
        candidate.fitness = function.valueAt(decoder.decode(candidate));
        neighbor.fitness = function.valueAt(decoder.decode(neighbor));

        double delta = neighbor.fitness - candidate.fitness;
        if (minimize) {
            delta *= -1;
        }

        return delta;
    }

}
