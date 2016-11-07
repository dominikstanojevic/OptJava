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
 * Created by Dominik on 25.10.2016..
 */
public class GenerationalElitistGA<T extends AbstractSolution> implements IAlgorithm<T> {
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

    public GenerationalElitistGA(
            IDecoder<T> decoder, ICrossoverOperator<T> crossoverOperator, IMutationOperator<T> mutationOperator,
            int populationSize, Supplier<T> solutionSupplier, double minError, int maxGenerations,
            ISelection<T> selection, double elitistRate, IFunction function, boolean minimize) {
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
    }

    @Override
    public T run() {
        int generation = 0;

        Population<T> population = new Population(populationSize);
        population.generate(RANDOM, solutionSupplier);

        evaluate(population);
        while (!conditionsSatisfied(generation, population)) {
            Population<T> newPop = new Population(populationSize);

            addToNewPopulation(population, newPop);

            while (newPop.size() < populationSize) {
                Pair<T, T> parents = getParents(population);
                Pair<T, T> children = crossoverOperator.getChildren(parents, RANDOM);
                mutate(children);

                newPop.addSolution(children.first);
                newPop.addSolution(children.second);
            }

            population = newPop;
            evaluate(population);
            generation++;

            T best = population.getBest();
            System.out.println(
                    "Generation: " + generation + ", " + best + ", error: " + function.valueAt(decoder.decode(best)));
        }

        return population.getBest();
    }

    private void mutate(Pair<T, T> children) {
        mutationOperator.mutate(children.first, RANDOM);
        mutationOperator.mutate(children.second, RANDOM);
    }

    private void addToNewPopulation(Population<T> old, Population<T> newPop) {
        int n = (int) Math.round(elitistRate * populationSize);
        for (int i = 0; i < n; i++) {
            newPop.addSolution(old.get(i));
        }
    }

    private Pair<T, T> getParents(Population<T> population) {
        T first = selection.selectBest(population, RANDOM);

        T second;
        do {
            second = selection.selectBest(population, RANDOM);
        } while (first.equals(second));

        return new Pair<>(first, second);
    }

    private boolean conditionsSatisfied(int generation, Population<T> population) {
        if (generation >= maxGenerations) {
            return true;
        }

        T best = population.getBest();
        double error = function.valueAt(decoder.decode(best));
        if (error <= minError) {
            return true;
        }

        return false;
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
}
