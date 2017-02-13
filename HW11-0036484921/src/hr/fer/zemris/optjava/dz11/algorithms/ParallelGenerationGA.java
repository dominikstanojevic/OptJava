package hr.fer.zemris.optjava.dz11.algorithms;

import hr.fer.zemris.optjava.dz11.ThreadPool;
import hr.fer.zemris.optjava.dz11.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz11.evaluators.IGAEvaluator;
import hr.fer.zemris.optjava.dz11.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz11.selections.ISelectionOperator;
import hr.fer.zemris.optjava.dz11.solutions.GASolution;
import hr.fer.zemris.optjava.dz11.solutions.Pair;
import hr.fer.zemris.optjava.dz11.solutions.Population;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * Created by Dominik on 22.1.2017..
 */
public class ParallelGenerationGA<T extends GASolution<?>> implements IAlgorithm<T> {
    private int populationSize;
    private int maxGenerations;
    private double merr;
    private IGAEvaluator<T> evaluator;
    private Supplier<T> supplier;
    private ThreadPool<Pair<T, T>> pool = new ThreadPool<>(Runtime.getRuntime().availableProcessors() + 1);
    private ISelectionOperator<T> selection;
    private ICrossoverOperator<T> crossover;
    private IMutationOperator<T> mutation;

    public ParallelGenerationGA(
            int populationSize, int maxGenerations, double merr, IGAEvaluator<T> evaluator, Supplier<T> supplier,
            ISelectionOperator<T> selection, ICrossoverOperator<T> crossover, IMutationOperator<T> mutation) {
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.merr = merr;
        this.evaluator = evaluator;
        this.supplier = supplier;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    @Override
    public T run() {
        Population<T> population = initPopulation();
        evaluatePopulation(population);
        T best = population.getBest();

        for (int i = 0; i < maxGenerations && best.fitness < merr; i++) {
            printBest(best, i);

            Population<T> newPopulation = new Population<>(populationSize);

            newPopulation.add(population.get(0));
            newPopulation.add(population.get(1));

            Population<T> tempPop = population;
            double jobs = (populationSize - newPopulation.size()) / 2 + 1;
            for (int t = 0; t < jobs; t++) {
                Callable<Pair<T, T>> callable = () -> {
                    Pair<T, T> parents = selectParents(tempPop);
                    Pair<T, T> children = crossover.getChildren(parents);
                    mutate(children);
                    evaluator.evaluate(children.first);
                    evaluator.evaluate(children.second);

                    return children;
                };
                pool.addJob(callable);
            }

            while (newPopulation.size() < populationSize) {
                try {
                    Pair<T, T> children = pool.getResult().get();
                    newPopulation.add(children.first);
                    if (newPopulation.size() == populationSize) {
                        break;
                    }
                    newPopulation.add(children.second);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            population = newPopulation;
            best = population.getBest();
        }

        pool.shutdown();

        return best;
    }

    private void addToPopulation(T solution, Population<T> population) {
        if (population.size() < populationSize) {
            population.add(solution);
        }
    }

    private void mutate(Pair<T, T> children) {
        mutation.mutate(children.first);
        mutation.mutate(children.second);
    }

    private Pair<T, T> selectParents(Population<T> population) {
        T first = selection.selectBest(population);
        T second = selection.selectBest(population);
        while (first == second) {
            second = selection.selectBest(population);
        }

        return new Pair<>(first, second);
    }

    private void printBest(T best, int iteration) {
        System.out.println("Iteration: " + iteration + ", best: " + best.fitness);
    }

    private void evaluatePopulation(Population<T> population) {
        for (T solution : population) {
            evaluator.evaluate(solution);
        }
    }

    private Population<T> initPopulation() {
        Population<T> population = new Population<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            T solution = supplier.get();
            solution.randomize();
            population.add(solution);
        }

        return population;
    }
}
