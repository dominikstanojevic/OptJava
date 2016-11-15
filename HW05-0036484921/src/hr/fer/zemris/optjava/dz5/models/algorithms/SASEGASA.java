package hr.fer.zemris.optjava.dz5.models.algorithms;

import hr.fer.zemris.optjava.dz5.models.IDecoder;
import hr.fer.zemris.optjava.dz5.models.IFunction;
import hr.fer.zemris.optjava.dz5.models.Population;
import hr.fer.zemris.optjava.dz5.models.cfplans.ICompFactorPlan;
import hr.fer.zemris.optjava.dz5.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz5.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz5.models.selections.ISelection;
import hr.fer.zemris.optjava.dz5.models.solutions.AbstractSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * Created by Dominik on 11.11.2016..
 */
public class SASEGASA<T extends AbstractSolution> implements IAlgorithm<T> {
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    private int populationSize;
    private int noOfVillages;

    private double succRatio;
    private double maxSelPress;
    private int maxIterations;
    private ICompFactorPlan plan;

    private IFunction function;
    private boolean minimize;
    private Function<Random, T> supplier;
    private IDecoder<T> decoder;
    private ISelection<T> selection;
    private ICrossoverOperator<T> crossover;
    private IMutationOperator<T> mutation;

    public SASEGASA(
            int populationSize, int noOfVillages, double succRatio, double maxSelPress, int maxIterations,
            ICompFactorPlan plan, IFunction function, boolean minimize, Function<Random, T> supplier,
            IDecoder<T> decoder, ISelection<T> selection, ICrossoverOperator<T> crossover,
            IMutationOperator<T> mutation) {
        this.populationSize = populationSize;
        this.noOfVillages = noOfVillages;
        this.succRatio = succRatio;
        this.maxSelPress = maxSelPress;
        this.maxIterations = maxIterations;
        this.plan = plan;
        this.function = function;
        this.minimize = minimize;
        this.supplier = supplier;
        this.decoder = decoder;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    @Override
    public T run() {
        Population<T> population = new Population<>(populationSize);
        population.fill(OSGA.RANDOM, supplier);

        int noOfVillages = this.noOfVillages;

        while (noOfVillages > 0) {
            int subPopSize = populationSize / noOfVillages;

            List<Callable<Population<T>>> callables = new ArrayList<>();
            int last = 0;
            for (int i = 0; i < noOfVillages; i++) {
                int popSize = subPopSize;
                if (i == noOfVillages - 1) {
                    popSize = populationSize - (noOfVillages - 1) * subPopSize;
                }

                Population<T> subPopulation = population.getSubPopulation(last, last + popSize);
                last += popSize;

                Callable<Population<T>> village =
                        new OSGA<>(popSize, succRatio, maxSelPress, maxIterations, plan, function, minimize,
                                subPopulation, decoder, selection, crossover, mutation);
                callables.add(village);
            }

            List<Future<Population<T>>> results;
            try {
                results = executor.invokeAll(callables);
            } catch (InterruptedException e) {
                throw new RuntimeException("jebiga");
            }

            population = new Population<>(populationSize);
            for (Future<Population<T>> pop : results) {
                try {
                    Population<T> p = pop.get();
                    population.addPopulation(p);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(-1);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }

            printBest(population, noOfVillages);

            noOfVillages--;
        }

        executor.shutdown();
        return population.getBest();
    }

    private void printBest(Population<T> population, int iteration) {
        T best = population.getBest();

        System.out.println("Villages: " + iteration + ", best: " + best + ", fitness: " + best.fitness);
    }
}
