package hr.fer.zemris.optjava.dz10.models.algorithms;

import hr.fer.zemris.optjava.dz10.models.IFunction;
import hr.fer.zemris.optjava.dz10.models.Pair;
import hr.fer.zemris.optjava.dz10.models.Population;
import hr.fer.zemris.optjava.dz10.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz10.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz10.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz10.models.selections.ISelection;
import hr.fer.zemris.optjava.dz10.models.solutions.AbstractSolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by Dominik on 3.1.2017..
 */
public class NSGAII<T extends AbstractSolution> {
    private static final Random RANDOM = new Random();

    private int maxGenerations;
    private int populationSize;
    private IMOOPProblem<T> evaluator;
    private IDecoder<T> decoder;
    private Function<Random, T> solutionSupplier;
    private ISelection<T> selection;
    private ICrossoverOperator<T> crossoverOperator;
    private IMutationOperator<T> mutationOperator;

    public NSGAII(
            int maxGenerations, int populationSize, IMOOPProblem<T> evaluator, IDecoder<T> decoder,
            Function<Random, T> solutionSupplier, ISelection<T> selection, ICrossoverOperator<T> crossoverOperator,
            IMutationOperator<T> mutationOperator) {
        this.maxGenerations = maxGenerations;
        this.populationSize = populationSize;
        this.evaluator = evaluator;
        this.decoder = decoder;
        this.solutionSupplier = solutionSupplier;
        this.selection = selection;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
    }

    public List<Population<T>> run() {
        Population<T> population = initializePopulation();
        List<Population<T>> fronts = fastNonDominatedSorting(population);
        for (Population<T> pop : fronts) {
            crowdingDistanceAssignment(pop);
        }
        Population<T> newPop = makeNewPopulation(population);

        for (int t = 0; t < maxGenerations; t++) {
            population = Population.mergePopulations(population, newPop);
            fronts = fastNonDominatedSorting(population);

            Population<T> nextPopulation = new Population<>(populationSize);
            int i = 0;
            Population<T> currentFront = fronts.get(i);
            while (nextPopulation.size() + currentFront.size() <= populationSize) {
                crowdingDistanceAssignment(currentFront);
                nextPopulation.addAll(currentFront);
                i++;
                currentFront = fronts.get(i);
            }

            crowdingDistanceAssignment(currentFront);
            currentFront.sort();
            nextPopulation.addAll(currentFront);
            newPop = makeNewPopulation(nextPopulation);
            population = nextPopulation;
        }

        return fastNonDominatedSorting(population);
    }

    private Population<T> makeNewPopulation(Population<T> population) {
        Population<T> newPop = new Population<>(populationSize);
        while (newPop.size() < populationSize) {
            Pair<T, T> parents = selectParents(population);
            Pair<T, T> children = crossoverOperator.getChildren(parents, RANDOM);

            mutationOperator.mutate(children.first, RANDOM);
            if(children.first.satisfiesConstraints()) {
                newPop.addSolution(children.first);
            }
            if (newPop.size() >= populationSize) {
                break;
            }

            mutationOperator.mutate(children.second, RANDOM);
            if(children.second.satisfiesConstraints()) {
                newPop.addSolution(children.second);
            }
        }

        return newPop;
    }

    private Pair<T, T> selectParents(Population<T> population) {
        T first = selection.selectBest(population, RANDOM);
        T second = selection.selectBest(population, RANDOM);
        while (first == second) {
            second = selection.selectBest(population, RANDOM);
        }

        return new Pair<>(first, second);
    }

    private void crowdingDistanceAssignment(Population<T> population) {
        int length = population.size();
        population.forEach(s -> s.distance = 0);

        for (int m = 0, n = evaluator.getNumberOfObjectives(); m < n; m++) {
            int index = m;
            IFunction function = evaluator.getFunction(index);
            population.sort(Comparator.comparingDouble(s -> s.values[index]));

            population.get(0).distance = population.get(length - 1).distance = Double.POSITIVE_INFINITY;
            for (int i = 1, t = length - 1; i < t; i++) {
                population.get(i).distance +=
                        (population.get(i + 1).values[m] - population.get(i - 1).values[m]) / function.getRange();
            }
        }
    }

    private List<Population<T>> fastNonDominatedSorting(Population<T> population) {
        List<Population<T>> fronts = new ArrayList<>();
        Population<T> first = new Population<>(population.size());
        Map<T, Set<T>> dominatingMap = new HashMap<>();
        Map<T, Integer> dominationCountMap = new HashMap<>();

        population.forEach(s -> s.values = evaluator.evaluateSolution(s));

        for (T solution : population) {
            Set<T> dominating = new HashSet<>();
            int dominationCount = 0;

            for (T other : population) {
                if (isDominatedBy(other, solution)) {
                    dominating.add(other);
                } else if (isDominatedBy(solution, other)) {
                    dominationCount++;
                }
            }
            if (dominationCount == 0) {
                solution.fitness = 1;
                first.addSolution(solution);
            }
            dominatingMap.put(solution, dominating);
            dominationCountMap.put(solution, dominationCount);
        }

        int i = 1;
        Population<T> currentFront = first;
        while (currentFront.size() != 0) {
            fronts.add(currentFront);
            Population<T> nextFront = new Population<>(population.size());
            for (T solution : currentFront) {
                Set<T> dominating = dominatingMap.get(solution);
                for (T other : dominating) {
                    int dominationCount = dominationCountMap.get(other);
                    dominationCount--;
                    dominationCountMap.put(other, dominationCount);
                    if (dominationCount == 0) {
                        other.fitness = i + 1;
                    }
                    nextFront.addSolution(other);
                }
            }

            currentFront = nextFront;
            i++;
        }

        return fronts;
    }

    private Population<T> initializePopulation() {
        Population<T> population = new Population<>(populationSize);
        population.fill(RANDOM, solutionSupplier);
        return population;
    }

    private boolean isDominatedBy(T first, T second) {
        boolean dominate = false;

        for (int i = 0; i < first.values.length; i++) {
            if (first.values[i] < second.values[i]) {
                return false;
            } else if (first.values[i] > second.values[i]) {
                dominate = true;
            }
        }

        return dominate;
    }
}
