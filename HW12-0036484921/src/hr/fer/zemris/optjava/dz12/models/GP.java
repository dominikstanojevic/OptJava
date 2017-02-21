package hr.fer.zemris.optjava.dz12.models;

import hr.fer.zemris.optjava.dz12.models.operators.OnePointCrossover;
import hr.fer.zemris.optjava.dz12.models.operators.OnePointMutation;
import hr.fer.zemris.optjava.dz12.models.operators.TournamentSelection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 12.2.2017..
 */
public class GP {
    private int populationSize;
    private int maxGenerations;
    private double minFitness;
    private double crossoverProbability;
    private double mutationProbability;
    private Evaluator evaluator;
    private OnePointMutation mutationOperator;
    private OnePointCrossover crossoverOperator;
    private TournamentSelection selectionOperator;

    public GP(
            int populationSize, int maxGenerations, double crossoverProbability, double mutationProbability,
            double minFitness, Evaluator evaluator, OnePointMutation mutationOperator,
            OnePointCrossover crossoverOperator, TournamentSelection selectionOperator) {
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.minFitness = minFitness;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.evaluator = evaluator;
        this.mutationOperator = mutationOperator;
        this.crossoverOperator = crossoverOperator;
        this.selectionOperator = selectionOperator;
    }

    public Ant run(List<Ant> population) {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < maxGenerations; i++) {
            evaluate(population, pool);
            Ant best = getBest(population);
            System.out.println("Iteration: " + i + ", fitness: " + best.fitness);

            if (best.fitness >= minFitness) {
                pool.shutdown();
                return best;
            }

            population = createGeneration(population);
        }

        evaluate(population, pool);
        Ant best = getBest(population);

        pool.shutdown();
        return best;
    }

    private List<Ant> createGeneration(List<Ant> population) {
        List<Ant> newPop = new ArrayList<>();
        newPop.add(getBest(population));

        Random random = ThreadLocalRandom.current();
        while (newPop.size() < populationSize) {
            double probability = random.nextDouble();
            if (probability <= crossoverProbability) {
                Pair<Ant, Ant> children = crossover(population);
                if (children.first != null) {
                    newPop.add(children.first);
                }
                if (children.second != null && newPop.size() < populationSize) {
                    newPop.add(children.second);
                }
            } else if (probability - crossoverProbability <= mutationProbability) {
                Ant mutated = mutate(population);
                if (mutated != null) {
                    newPop.add(mutated);
                }
            } else {
                newPop.add(selectionOperator.select(population));
            }
        }

        return newPop;
    }

    private Ant mutate(List<Ant> population) {
        Ant selected = selectionOperator.select(population);
        return mutationOperator.mutate(selected);
    }

    private Pair<Ant, Ant> crossover(List<Ant> population) {
        Ant firstParent = selectionOperator.select(population);
        Ant secondParent = selectionOperator.select(population);

        return crossoverOperator.getChildren(new Pair<>(firstParent, secondParent));
    }

    private void evaluate(List<Ant> population, ExecutorService pool) {
        List<Callable<Void>> callables = new ArrayList<>();
        for (Ant ant : population) {
            if (ant.fitness != null) {
                continue;
            }

            Callable<Void> callable = () -> {
                evaluator.evaluate(ant);
                return null;
            };
            callables.add(callable);
        }

        try {
            List<Future<Void>> results = pool.invokeAll(callables);
            for (Future<Void> result : results) {
                result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Ant getBest(List<Ant> population) {
        population.sort(Comparator.reverseOrder());
        return population.get(0);
    }
}
