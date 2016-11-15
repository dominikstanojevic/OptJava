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

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by Dominik on 11.11.2016..
 */
public class OSGA<T extends AbstractSolution> implements Callable<Population<T>>, IAlgorithm<T> {
    public static final Random RANDOM = new Random();

    private int populationSize;
    private double succRatio;
    private double maxSelPress;
    private int maxIterations;
    private ICompFactorPlan plan;

    private IFunction function;
    private boolean minimize;
    private Population<T> population;
    private IDecoder<T> decoder;
    private ISelection<T> selection;
    private ICrossoverOperator<T> crossover;
    private IMutationOperator<T> mutation;

    public OSGA(
            int populationSize, double succRatio, double maxSelPress, int maxIterations, ICompFactorPlan plan,
            IFunction function, boolean minimize, Population<T> population, IDecoder<T> decoder,
            ISelection<T> selection, ICrossoverOperator<T> crossover, IMutationOperator<T> mutation) {
        this.populationSize = populationSize;
        this.succRatio = succRatio;
        this.maxSelPress = maxSelPress;
        this.maxIterations = maxIterations;
        this.plan = plan;
        this.function = function;
        this.minimize = minimize;
        this.population = population;
        this.decoder = decoder;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    @Override
    public Population<T> call() throws Exception {
        evaluate(population);

        double actualSelPress = 1;

        int iterations = 0;
        double compFactor = plan.getCF(iterations);

        while ((iterations < maxIterations) && (actualSelPress < maxSelPress)) {
            Population<T> newPop = new Population<>(populationSize);
            Population<T> pool = new Population<>();

            while ((newPop.size() < succRatio * populationSize) &&
                   (newPop.size() + pool.size() < (populationSize * maxSelPress))) {

                Pair<T, T> parents = getParents(population);
                T child = generateChild(parents);
                child.fitness = calculateFitness(child);

                if (isBetter(parents, child, compFactor)) {
                    newPop.addSolution(child);
                } else {
                    pool.addSolution(child);
                }
            }

            actualSelPress = ((double) newPop.size() + pool.size()) / populationSize;

            while (newPop.size() < populationSize) {
                if (pool.size() == 0) {
                    Pair<T, T> parents = getParents(population);
                    T child = generateChild(parents);
                    child.fitness = calculateFitness(child);
                    newPop.addSolution(child);
                } else {
                    newPop.addSolution(pool.remove(RANDOM.nextInt(pool.size())));
                }
            }

            population = newPop;

            iterations++;
            compFactor = plan.getCF(iterations);
        }

        return population;
    }

    private boolean isBetter(Pair<T, T> parents, T child, double compFactor) {
        T better, worse;
        if (parents.first.fitness < parents.second.fitness) {
            better = parents.second;
            worse = parents.first;
        } else {
            better = parents.first;
            worse = parents.second;
        }

        double value = worse.fitness + compFactor * Math.abs(better.fitness - worse.fitness);
        return child.fitness > value;
    }

    private T generateChild(Pair<T, T> parents) {
        T child = crossover.getChild(parents, RANDOM);
        mutation.mutate(child, RANDOM);

        return child;
    }

    private Pair<T, T> getParents(Population<T> population) {
        T first = selection.selectBest(population, RANDOM);

        T second = population.get(RANDOM.nextInt(populationSize));
        while(first.equals(second)) {
            second = population.get(RANDOM.nextInt(populationSize));
        }

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

    @Override
    public T run() {
        try {
            Population<T> finalPopulation = call();
            return finalPopulation.getBest();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in algorithm.");
        }
    }
}
