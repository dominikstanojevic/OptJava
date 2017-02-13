package hr.fer.zemris.optjava.dz4.models.algorithms;

import hr.fer.zemris.optjava.dz9.models.Pair;
import hr.fer.zemris.optjava.dz9.models.Population;
import hr.fer.zemris.optjava.dz9.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz9.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz9.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz9.models.selections.ISelection;
import hr.fer.zemris.optjava.dz9.models.selections.RouletteWheelSelection;
import hr.fer.zemris.optjava.dz9.models.solutions.AbstractSolution;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Created by Dominik on 29.12.2016..
 */
public class NSGA<T extends AbstractSolution> {
    private static final Random RANDOM = ThreadLocalRandom.current();

    private IDecoder<T> decoder;
    private IMOOPProblem<T> evaluation;
    private Function<Random, T> solutionSupplier;
    private ISelection<T> selectionOperator = new RouletteWheelSelection<>();
    private IMutationOperator<T> mutationOperator;
    private ICrossoverOperator<T> crossoverOperator;
    private Distance distanceType;

    private int maxGenerations;
    private int populationSize;
    private double nicheRadius;
    private double alpha;

    public NSGA(
            IDecoder<T> decoder, IMOOPProblem<T> evaluation, Function<Random, T> solutionSupplier,
            IMutationOperator<T> mutationOperator, ICrossoverOperator crossoverOperator, Distance distanceType,
            int maxGenerations, int populationSize, double nicheRadius, double alpha) {
        this.decoder = decoder;
        this.evaluation = evaluation;
        this.solutionSupplier = solutionSupplier;
        this.mutationOperator = mutationOperator;
        this.crossoverOperator = crossoverOperator;
        this.distanceType = distanceType;
        this.maxGenerations = maxGenerations;
        this.populationSize = populationSize;
        this.nicheRadius = nicheRadius;
        this.alpha = alpha;
    }

    public Population<T> run() {
        Population<T> population = new Population<>(populationSize);
        population.fill(RANDOM, solutionSupplier);

        evaluatePopulation(population);

        for (int i = 0; i < maxGenerations; i++) {
            Population<T> newPop = new Population<>(populationSize);
            while (newPop.size() < populationSize) {
                Pair<T, T> parents = selectParents(population);
                Pair<T, T> children = crossoverOperator.getChildren(parents, RANDOM);
                mutateChildren(children);

                if(children.first.satisfiesConstraints()) {
                    newPop.addSolution(children.first);
                }
                if (children.second.satisfiesConstraints()) {
                    newPop.addSolution(children.second);
                }
            }

            population = newPop;
            evaluatePopulation(population);
        }

        return getNondominated(population);
    }

    private void mutateChildren(Pair<T, T> children) {
        mutationOperator.mutate(children.first, RANDOM);
        mutationOperator.mutate(children.second, RANDOM);
    }

    private Pair<T, T> selectParents(Population<T> population) {
        T first = selectionOperator.selectBest(population, RANDOM);

        T second = selectionOperator.selectBest(population, RANDOM);
        while (second == first) {
            second = selectionOperator.selectBest(population, RANDOM);
        }

        return new Pair<>(first, second);
    }

    private void evaluatePopulation(Population<T> population) {
        population.forEach(s -> s.values = evaluation.evaluateSolution(s));

        Population<T> remain = new Population<>(population);
        double dummyFitness = population.getCapacity();

        while (remain.size() != 0) {
            Population<T> nondominated = getNondominated(remain);
            remain.removeAll(nondominated);

            double min = calculateFitness(nondominated, dummyFitness);
            dummyFitness = min - 0.1;
        }
    }

    private double calculateFitness(Population<T> nondominated, double dummyFitness) {
        double min = Double.POSITIVE_INFINITY;

        for (T solution : nondominated) {
            double nicheCount = calculateNicheCount(solution, nondominated);

            solution.fitness = dummyFitness / nicheCount;
            if (solution.fitness < min) {
                min = solution.fitness;
            }
        }

        return min;
    }

    private double calculateNicheCount(T solution, Population<T> nondominated) {
        double total = 0;
        for (T element : nondominated) {
            double distance = distanceType.distance(decoder, solution, element);

            if (distance < nicheRadius) {
                total += 1 - Math.pow(distance / nicheRadius, alpha);
            }
        }

        return total;
    }

    private Population<T> getNondominated(Population<T> population) {
        int size = population.size();
        Population<T> nondominated = new Population<>(size);

        for (int i = 0; i < size; i++) {
            T solution = population.get(i);
            boolean nond = true;

            for (int j = 0; j < size; j++) {
                T test = population.get(j);

                if (isDominatedBy(solution, test)) {
                    nond = false;
                    break;
                }
            }

            if (nond) {
               nondominated.addSolution(solution);
            }
        }

        return nondominated;
    }

    private boolean isDominatedBy(T first, T second) {
        boolean dominate = false;

        for (int i = 0; i < first.values.length; i++) {
            if (first.values[i] < second.values[i]) {
                return false;
            } else if (first.values[i] > second.values[i]) {
                dominate = true;
            }
        }

        return dominate;
    }
}
