package hr.fer.zemris.optjava.dz8.algorithms;

import hr.fer.zemris.optjava.dz8.models.Evaluator;
import hr.fer.zemris.optjava.dz8.models.Solution;
import hr.fer.zemris.optjava.dz8.models.neural.Utils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Dominik on 18.12.2016..
 */
public class DE {
    private int solutionSize;
    private int populationSize;
    private int maxIterations;
    private double merr;
    private Evaluator evaluator;
    private double scaleFactor;
    private double crossoverProbability;

    public DE(
            int solutionSize, int populationSize, int maxIterations, double merr, Evaluator evaluator,
            double scaleFactor, double crossoverProbability) {
        this.solutionSize = solutionSize;
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.merr = merr;
        this.evaluator = evaluator;
        this.scaleFactor = scaleFactor;
        this.crossoverProbability = crossoverProbability;
    }

    public Solution run() {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        List<Solution> population = initialize();
        evaluatePopulation(population, pool);
        Collections.sort(population, Collections.reverseOrder());

        for (int i = 0; i < maxIterations && merr < 1 / population.get(0).fitness; i++) {
            List<Solution> newPopulation = Collections.synchronizedList(new ArrayList<>());
            List<Solution> tempPop = population;

            List<Callable<Void>> callables = new ArrayList<>();
            for (Solution solution : population) {
                Callable<Void> callable = () -> {
                    Solution target = solution;
                    RealVector mutant = mutate(tempPop, target);
                    Solution trial = crossover(target, mutant);
                    trial.fitness = evaluator.evaluate(trial);

                    if (trial.fitness >= target.fitness) {
                        newPopulation.add(trial);
                    } else {
                        newPopulation.add(target);
                    }

                    return null;
                };

                callables.add(callable);
            }

            try {
                List<Future<Void>> results = pool.invokeAll(callables);
                for (Future<Void> result : results) {
                    result.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            population = newPopulation;
            Collections.sort(population, Collections.reverseOrder());

            Solution best = population.get(0);
            System.out.println("Iteration: " + i + ", best: " + best);
        }

        pool.shutdown();
        return population.get(0);
    }

    private Solution crossover(Solution target, RealVector mutant) {
        double[] trial = new double[solutionSize];
        int point = Utils.RANDOM.nextInt(solutionSize);

        for (int i = 0; i < solutionSize; i++) {
            if (Utils.RANDOM.nextDouble() <= crossoverProbability || i == point) {
                trial[i] = mutant.getEntry(i);
            } else {
                trial[i] = target.values.getEntry(i);
            }
        }

        return new Solution(new ArrayRealVector(trial));
    }

    private RealVector mutate(List<Solution> population, Solution target) {
        Solution base = population.get(Utils.RANDOM.nextInt(populationSize));
        while (base == target) {
            base = population.get(Utils.RANDOM.nextInt(populationSize));
        }

        Solution firstDifferential = population.get(Utils.RANDOM.nextInt(populationSize));
        while (firstDifferential == base || firstDifferential == target) {
            firstDifferential = population.get(Utils.RANDOM.nextInt(populationSize));
        }

        Solution secondDifferential = population.get(Utils.RANDOM.nextInt(populationSize));
        while (secondDifferential == firstDifferential || secondDifferential == base || secondDifferential == target) {
            secondDifferential = population.get(Utils.RANDOM.nextInt(populationSize));
        }

        RealVector diff = firstDifferential.values.subtract(secondDifferential.values);
        return base.values.add(diff.mapMultiplyToSelf(scaleFactor));
    }

    private void evaluatePopulation(List<Solution> population, ExecutorService pool) {
        List<Callable<Void>> callables = new ArrayList<>();
        for(Solution solution : population) {
            Callable<Void> callable = () -> {
                solution.fitness = evaluator.evaluate(solution);
                return null;
            };

            callables.add(callable);
        }

        try {
            List<Future<Void>> results = pool.invokeAll(callables);
            for(Future<Void> result : results) {
                result.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private List<Solution> initialize() {
        List<Solution> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            population.add(Solution.randomize(solutionSize));
        }

        return population;
    }
}
