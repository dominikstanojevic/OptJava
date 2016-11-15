package hr.fer.zemris.optjava.dz5.models.algorithms;

import hr.fer.zemris.optjava.dz5.models.IDecoder;
import hr.fer.zemris.optjava.dz5.models.IFunction;
import hr.fer.zemris.optjava.dz5.models.Pair;
import hr.fer.zemris.optjava.dz5.models.Population;
import hr.fer.zemris.optjava.dz5.models.cfplans.ICompFactorPlan;
import hr.fer.zemris.optjava.dz5.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz5.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz5.models.selections.ISelection;
import hr.fer.zemris.optjava.dz5.models.solutions.AbstractSolution;
import hr.fer.zemris.optjava.dz5.part1.RAPGAPopulation;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by Dominik on 11.11.2016..
 */
public class RAPGA<T extends AbstractSolution> implements IAlgorithm<T> {
    private static final Random RANDOM = new Random();

    private int maxPop;
    private int minPop;
    private double succRatio;
    private ICompFactorPlan cfPlan;
    private double MaxSelPress;
    private int maxIterations;
    private boolean minimize;

    private IDecoder<T> decoder;
    private ISelection<T> selection;
    private List<ICrossoverOperator<T>> crossovers;
    private List<IMutationOperator<T>> mutations;
    private Function<Random, T> solutionSupplier;
    private IFunction function;

    public RAPGA(
            int maxPop, int minPop, double succRatio, ICompFactorPlan cfPlan, double maxSelPress, int maxIterations,
            boolean minimize, IDecoder<T> decoder, ISelection<T> selection, List<ICrossoverOperator<T>> crossovers,
            List<IMutationOperator<T>> mutations, Function<Random, T> solutionSupplier, IFunction function) {
        this.maxPop = maxPop;
        this.minPop = minPop;
        this.succRatio = succRatio;
        this.cfPlan = cfPlan;
        MaxSelPress = maxSelPress;
        this.maxIterations = maxIterations;
        this.minimize = minimize;
        this.decoder = decoder;
        this.selection = selection;
        this.crossovers = crossovers;
        this.mutations = mutations;
        this.solutionSupplier = solutionSupplier;
        this.function = function;
    }

    @Override
    public T run() {
        RAPGAPopulation<T> pop = new RAPGAPopulation<>(maxPop);
        pop.fill(RANDOM, solutionSupplier);

        evaluate(pop);

        double actSelPress = 1;
        int i = 0;
        double compFactor = cfPlan.getCF(i);

        printBest(pop, i);

        while ((i < maxIterations) && (actSelPress < MaxSelPress) && pop.size() >= minPop) {
            RAPGAPopulation<T> newPop = new RAPGAPopulation<>(maxPop);
            int noOfWorse = 0;

            while ((newPop.size() < succRatio * pop.size()) && (newPop.size() + noOfWorse < MaxSelPress * pop.size())) {
                Pair<T, T> parents = getParents(pop);

                T child = generateChild(parents);
                child.fitness = calculateFitness(child);

                if (isBetter(parents, child, compFactor)) {
                    newPop.addSolution(child);
                } else {
                    noOfWorse++;
                }

                actSelPress = ((double) newPop.size() + noOfWorse) / pop.size();
            }

            i++;
            compFactor = cfPlan.getCF(i);
            pop = newPop;

            printBest(pop, i);
        }

        return pop.getBest();
    }

    private void printBest(Population<T> population, int iteration) {
        T best = population.getBest();

        System.out.println("Iteration: " + iteration + ", best: " + best + ", fitness: " + best.fitness);
    }

    private boolean isBetter(Pair<T, T> parents, T child, double CompFactor) {
        T better, worse;
        if (parents.first.fitness < parents.second.fitness) {
            better = parents.second;
            worse = parents.first;
        } else {
            better = parents.first;
            worse = parents.second;
        }

        double parentsFitness = worse.fitness + CompFactor * Math.abs(better.fitness - worse.fitness);
        return child.fitness > parentsFitness;
    }

    private Pair<T, T> getParents(Population<T> population) {
        T first = selection.selectBest(population, RANDOM);
        T second = population.get(RANDOM.nextInt(population.size()));

        return new Pair<>(first, second);
    }

    private void evaluate(Population<T> population) {
        for (T solution : population) {
            solution.fitness = calculateFitness(solution);
        }
    }

    private double calculateFitness(T solution) {
        double value = function.valueAt(decoder.decode(solution));
        if (minimize) {
            value *= -1;
        }

        return value;
    }

    private T generateChild(Pair<T, T> parents) {
        ICrossoverOperator<T> crossover = crossovers.get(RANDOM.nextInt(crossovers.size()));
        T child = crossover.getChild(new Pair<>(parents.first, parents.second), RANDOM);

        IMutationOperator<T> mutation = mutations.get(RANDOM.nextInt(mutations.size()));
        mutation.mutate(child, RANDOM);

        return child;
    }

}
