package hr.fer.zemris.optjava.dz4.models.algorithms;

import hr.fer.zemris.optjava.dz4.models.IFunction;
import hr.fer.zemris.optjava.dz4.models.Pair;
import hr.fer.zemris.optjava.dz4.models.Population;
import hr.fer.zemris.optjava.dz4.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz4.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz4.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz4.models.selections.ISelection;
import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;

import java.util.Random;
import java.util.function.Function;

/**
 * Created by Dominik on 7.11.2016..
 */
public class SteadyStateGA<T extends AbstractSolution> implements IAlgorithm<T> {
    private static final Random RANDOM = new Random();

    private IDecoder<T> decoder;
    private ICrossoverOperator<T> crossoverOperator;
    private IMutationOperator<T> mutationOperator;
    private int populationSize;
    private Function<Random, T> solutionSupplier;
    private int maxGenerations;
    private ISelection<T> selection;
    private IFunction function;
    private boolean minimize;
    private boolean switchParent;

    public SteadyStateGA(
            IDecoder<T> decoder, ICrossoverOperator<T> crossoverOperator, IMutationOperator<T> mutationOperator,
            int populationSize, Function<Random, T> solutionSupplier, int maxGenerations,
            ISelection<T> selection, IFunction function, boolean minimize, boolean switchParent) {
        this.decoder = decoder;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.populationSize = populationSize;
        this.solutionSupplier = solutionSupplier;
        this.maxGenerations = maxGenerations;
        this.selection = selection;
        this.function = function;
        this.minimize = minimize;
        this.switchParent = switchParent;
    }

    @Override
    public T run() {
        Population<T> population = new Population<>(populationSize);
        population.fill(RANDOM, solutionSupplier);

        evaluate(population);

        T best = population.getBest();

        System.out.println("Initial: ");
        printSolution(best, 0);
        System.out.println("-----------------------------------------");

        int iteration = 1;
        while (!conditionsSatisfied(iteration)) {
            Pair<T, T> parents = getParents(population);
            Pair<T, T> children = crossoverOperator.getChildren(parents, RANDOM);

            mutate(children);
            evaluateChildren(children);

            switchSolutions(population, children.first);
            switchSolutions(population, children.second);

            T newBest = population.getBest();
            if (newBest.fitness > best.fitness) {
                best = newBest;
                printSolution(best, iteration);
            }
            iteration++;
        }

        return best;
    }

    private void printSolution(T solution, int iteration) {
        System.out.println("Iteration: " + iteration + ", fitness: " + solution.fitness + ", " + solution.toString());
    }

    private void switchSolutions(Population<T> population, T child) {
        T remove = selection.selectWorst(population, RANDOM);
        if (switchParent || child.fitness > remove.fitness) {
            population.removeSolution(remove);
            population.addSolution(child);
        }
    }

    private void mutate(Pair<T, T> children) {
        mutationOperator.mutate(children.first, RANDOM);
        mutationOperator.mutate(children.second, RANDOM);
    }

    private Pair<T, T> getParents(Population<T> population) {
        T first = selection.selectBest(population, RANDOM);

        T second = selection.selectBest(population, RANDOM);
        while (second.equals(first)) {
            second = selection.selectBest(population, RANDOM);
        }

        return new Pair<>(first, second);
    }

    private void evaluate(Population<T> population) {
        for (T solution : population) {
            solution.fitness = calculateFitness(solution);
        }

        population.sort();
    }

    private double calculateFitness(T solution) {
        double fitness = function.valueAt(decoder.decode(solution));
        if (minimize) {
            fitness *= -1;
        }

        return fitness;
    }

    private boolean conditionsSatisfied(int generation) {
        if (generation >= maxGenerations) {
            return true;
        }

        return false;
    }

    private void evaluateChildren(Pair<T, T> children) {
        children.first.fitness = calculateFitness(children.first);
        children.second.fitness = calculateFitness(children.second);
    }
}
