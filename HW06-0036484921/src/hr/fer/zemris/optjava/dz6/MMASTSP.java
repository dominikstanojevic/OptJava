package hr.fer.zemris.optjava.dz6;

import hr.fer.zemris.optjava.dz6.models.Ant;
import hr.fer.zemris.optjava.dz6.models.CityMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Dominik on 1.12.2016..
 */
public class MMASTSP {
    private static final Random RANDOM = new Random();

    private CityMap map;

    private int antsNumber;
    private double alpha;
    private double beta;
    private double ro;

    private int maxIterations;

    public MMASTSP(CityMap map, int antsNumber, double alpha, double beta, double ro, int maxIterations) {
        this.map = map;
        this.antsNumber = antsNumber;
        this.alpha = alpha;
        this.beta = beta;
        this.ro = ro;
        this.maxIterations = maxIterations;
    }

    public Ant run() {
        double tauMax, tauMin;
        double[][] trails = new double[map.numberOfCities][map.numberOfCities];

        Ant solution = new Ant();
        solution.visitedCities = new int[map.numberOfCities];

        visitGreedy(solution);

        tauMax = calculateTauMax(solution.length);
        tauMin = calculateTauMin(tauMax);
        for (double[] trailsRow : trails) {
            Arrays.fill(trailsRow, tauMax);
        }

        Ant[] colony = createColony();

        for (int i = 1; i <= maxIterations; i++) {
            antsTour(colony, trails);

            Ant best = Arrays.stream(colony).sorted(Comparator.comparingDouble(a -> a.length)).findFirst().get();

            if (best.length < solution.length) {
                solution.length = best.length;
                solution.visitedCities = best.visitedCities;

                tauMax = calculateTauMax(solution.length);
                tauMin = calculateTauMin(tauMax);
            }

            evaporateTrails(trails, tauMin);
            updateTrails(trails, tauMax, best);

            printCurrentBest(solution, i);
        }

        return solution;
    }

    private void antsTour(Ant[] colony, double[][] trails) {
        List<Callable<Void>> callableList = createCallable(colony, trails);
        try {
            List<Future<Void>> results = TSP.POOL.invokeAll(callableList);

            for (Future<Void> future : results) {
                future.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void evaporateTrails(double[][] trails, double tauMin) {
        for (int i = 0; i < map.numberOfCities; i++) {
            for (int j = i + 1; j < map.numberOfCities; j++) {
                double next = ro * trails[i][j];
                trails[i][j] = trails[j][i] = next < tauMin ? tauMin : next;
            }
        }
    }

    private void printCurrentBest(Ant solution, int iteration) {
        System.out.println("Current iteration: " + iteration);
        System.out.println(solution);
        System.out.println("--------------------------------------------------");
    }

    private void updateTrails(double[][] trails, double tauMax, Ant ant) {
        double delta = 1 / ant.length;

        for (int i = 0; i < map.numberOfCities - 1; i++) {
            int current = ant.visitedCities[i];
            int next = ant.visitedCities[i + 1];

            double result = trails[current][next] + delta;
            trails[current][next] = trails[next][current] = result > tauMax ? tauMax : result;
        }

        int current = ant.visitedCities[map.numberOfCities - 1];
        int next = ant.visitedCities[0];

        double result = trails[current][next] + delta;
        trails[current][next] = trails[next][current] = result > tauMax ? tauMax : result;
    }

    private void tour(Ant ant, double[][] trails) {
        ant.visitedCities = new int[map.numberOfCities];
        boolean[] visited = new boolean[map.numberOfCities];

        int start = RANDOM.nextInt(map.numberOfCities);
        ant.visitedCities[0] = start;
        visited[start] = true;

        for (int i = 0, n = map.numberOfCities - 1; i < n; i++) {
            int current = ant.visitedCities[i];

            int nextCity = chooseNextCity(current, visited, trails);
            ant.visitedCities[i + 1] = nextCity;
            visited[nextCity] = true;
        }

        ant.length = calculateDistance(ant.visitedCities);
    }

    private int chooseNextCity(int currentCity, boolean[] visited, double[][] trails) {
        double total = 0;
        Map<Integer, Double> probabilities = new LinkedHashMap<>();

        for (int i = 0; i < map.neighborhoodSize; i++) {
            int neighbor = map.closest[currentCity][i];

            if (visited[neighbor]) {
                continue;
            }

            double probability = Math.pow(trails[currentCity][neighbor], alpha) *
                                 Math.pow(map.heuristicInformation[currentCity][neighbor], beta);
            total += probability;
            probabilities.put(neighbor, probability);
        }

        if (probabilities.isEmpty()) {
            List<Integer> unvisited = new ArrayList<>();

            for (int i = map.neighborhoodSize; i < map.numberOfCities - 1; i++) {
                int neighbor = map.closest[currentCity][i];

                if (visited[neighbor]) {
                    continue;
                }

                unvisited.add(neighbor);
            }

            return unvisited.get(RANDOM.nextInt(unvisited.size()));
        }

        for (Map.Entry<Integer, Double> entry : probabilities.entrySet()) {
            probabilities.put(entry.getKey(), entry.getValue() / total);
        }

        double random = RANDOM.nextDouble();
        total = 0;
        for (Map.Entry<Integer, Double> entry : probabilities.entrySet()) {
            total += entry.getValue();
            if (random <= total) {
                return entry.getKey();
            }
        }

        throw new RuntimeException();
    }

    private void visitGreedy(Ant ant) {
        boolean[] visited = new boolean[map.numberOfCities];

        int start = RANDOM.nextInt(map.numberOfCities);
        ant.visitedCities[0] = start;
        visited[start] = true;

        for (int i = 1; i < map.numberOfCities; i++) {
            int temp = 0;
            int next = map.closest[ant.visitedCities[i - 1]][temp];
            while (visited[next]) {
                next = map.closest[ant.visitedCities[i - 1]][++temp];
            }

            ant.visitedCities[i] = next;
            visited[next] = true;
        }

        ant.length = calculateDistance(ant.visitedCities);
    }

    private double calculateDistance(int[] cities) {
        double distance = 0;

        for (int i = 0; i < map.numberOfCities - 1; i++) {
            distance += map.distances[cities[i]][cities[i + 1]];
        }
        distance += map.distances[cities[map.numberOfCities - 1]][cities[0]];

        return distance;
    }

    private double calculateTauMax(double distance) {
        return 1 / ((1 - ro) * distance);
    }

    private Ant[] createColony() {
        Ant[] ants = new Ant[antsNumber];
        for (int i = 0; i < antsNumber; i++) {
            ants[i] = new Ant();
        }

        return ants;
    }

    private double calculateTauMin(double tauMax) {
        return tauMax / (2 * map.numberOfCities);
    }

    private List<Callable<Void>> createCallable(Ant[] ants, double[][] trails) {
        List<Callable<Void>> callableList = new ArrayList<>();

        for (Ant ant : ants) {
            callableList.add(() -> {
                tour(ant, trails);
                return null;
            });
        }

        return callableList;
    }
}
