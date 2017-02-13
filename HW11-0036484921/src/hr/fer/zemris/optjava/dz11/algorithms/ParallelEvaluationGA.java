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
import java.util.function.Supplier;

/**
 * Created by Dominik on 22.1.2017..
 */
public class ParallelEvaluationGA<T extends GASolution<?>> implements IAlgorithm<T> {
    private int populationSize;
    private int maxGenerations;
    private double merr;
    private IGAEvaluator<T> evaluator;
    private Supplier<T> supplier;
    private ThreadPool<Void> pool = new ThreadPool<>(Runtime.getRuntime().availableProcessors() + 1);
    private ISelectionOperator<T> selection;
    private ICrossoverOperator<T> crossover;
    private IMutationOperator<T> mutation;

    public ParallelEvaluationGA(
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

            while (newPopulation.size() < populationSize) {
                Pair<T, T> parents = selectParents(population);
                Pair<T, T> children = crossover.getChildren(parents);
                mutate(children);

                addToPopulation(children.first, newPopulation);
                addToPopulation(children.second, newPopulation);
            }

            population = newPopulation;
            evaluatePopulation(population);
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
        while(first == second) {
            second = selection.selectBest(population);
        }


        return new Pair<>(first, second);
    }

    private void printBest(T best, int iteration) {
        System.out.println("Iteration: " + iteration + ", best: " + best.fitness);
    }

    private void evaluatePopulation(Population<T> population) {
        for (T solution : population) {
            Callable<Void> callable = () -> {
                evaluator.evaluate(solution);
                return null;
            };
            pool.addJob(callable);
        }

        for (int i = 0; i < populationSize; i++) {
            pool.getResult();
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
