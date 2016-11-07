package hr.fer.zemris.optjava.dz4.models.algorithms;

import hr.fer.zemris.optjava.dz4.models.IFunction;
import hr.fer.zemris.optjava.dz4.models.Pair;
import hr.fer.zemris.optjava.dz4.models.Population;
import hr.fer.zemris.optjava.dz4.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz4.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz4.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz4.models.selections.ISelection;
import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by Dominik on 7.11.2016..
 */
public class SteadyStateGA<T extends AbstractSolution> implements IAlgorithm<T> {
    private static final Random RANDOM = new Random();

    private IDecoder<T> decoder;
    private ICrossoverOperator<T> crossoverOperator;
    private IMutationOperator<T> mutationOperator;
    private int populationSize;
    private Supplier<T> solutionSupplier;
    private double minError;
    private int maxGenerations;
    private ISelection<T> selection;
    private double elitistRate;
    private IFunction function;
    private boolean minimize;
    private boolean switchParent;

    public SteadyStateGA(
            IDecoder<T> decoder, ICrossoverOperator<T> crossoverOperator, IMutationOperator<T> mutationOperator,
            int populationSize, Supplier<T> solutionSupplier, double minError, int maxGenerations,
            ISelection<T> selection, double elitistRate, IFunction function, boolean minimize, boolean switchParent) {
        this.decoder = decoder;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.populationSize = populationSize;
        this.solutionSupplier = solutionSupplier;
        this.minError = minError;
        this.maxGenerations = maxGenerations;
        this.selection = selection;
        this.elitistRate = elitistRate;
        this.function = function;
        this.minimize = minimize;
        this.switchParent = switchParent;
    }

    @Override
    public T run() {
        int generation = 0;

        Population<T> population = new Population<>(populationSize);
        population.fill(solutionSupplier);

        evaluate(population);

        System.out.println("Initial: ");
        printBest(population, 0);

        while (!conditionsSatisfied(generation, population)) {
            Pair<T, T> parents = getParents(population);
            Pair<T, T> children = crossoverOperator.getChildren(parents, RANDOM);

            mutate(children);
            evaluateChildren(children);

            switchSolutions(population, children.first);
            switchSolutions(population, children.second);

            printBest(population, generation);
            generation++;
        }

        return population.getBest();
    }

    private void printBest(Population<T> population, int generation) {
        T best = population.getBest();
        System.out.println("Iteration: " + generation + ", " + best.toString() + "fitness: " + best.fitness);
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
        Iterator<T> iterator = population.iterator();
        while (iterator.hasNext()) {
            T solution = iterator.next();

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

    private boolean conditionsSatisfied(int generation, Population<T> population) {
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
