package hr.fer.zemris.optjava.dz7.algorithms;

import hr.fer.zemris.optjava.dz7.ANNTrainer;
import hr.fer.zemris.optjava.dz7.Utils;
import hr.fer.zemris.optjava.dz7.models.Antibody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Dominik on 12.12.2016..
 */
public class CLONALG {
    private int repertoireSize;
    private int numberOfClones;
    private int generatedAntibodies;
    private int maxIterations;
    private Predicate<Antibody> stopCondition;
    private Supplier<Antibody> antibodySupplier;
    private double ro;
    private double sigma;

    public CLONALG(
            int repertoireSize, int numberOfClones, double generatedAntibodiesPercentage, int maxIterations,
            Predicate<Antibody> stopCondition, Supplier<Antibody> antibodySupplier, double ro, double sigma) {
        this.repertoireSize = repertoireSize;
        this.numberOfClones = numberOfClones;
        this.generatedAntibodies = (int) generatedAntibodiesPercentage * repertoireSize;
        this.maxIterations = maxIterations;
        this.stopCondition = stopCondition;
        this.antibodySupplier = antibodySupplier;
        this.ro = ro;
        this.sigma = sigma;
    }

    public Antibody run() {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        List<Antibody> repertoire = createRepertoire();
        evaluateRepertoire(repertoire, pool);

        Antibody best = findBest(repertoire);

        for (int i = 0; i < maxIterations && !stopCondition.test(best); i++) {
            List<Antibody> parents = select(repertoire);

            List<Antibody> newAntibodies = createNewAntibodies(parents, pool);

            repertoire = insert(repertoire, newAntibodies);
            List<Antibody> randomlyGenerated = generate();
            replace(repertoire, randomlyGenerated);

            best = findBest(repertoire);
            printBest(best, i);
        }

        pool.shutdown();
        return best;
    }

    private void printBest(Antibody best, int iteration) {
        System.out.println("Iteration: " + iteration + ", cost: " + 1. / best.affinity);
    }

    private List<Antibody> generate() {
        List<Antibody> generated = new ArrayList<>();
        for (int i = 0; i < generatedAntibodies; i++) {
            generated.add(antibodySupplier.get());
        }

        return generated;
    }

    private List<Antibody> insert(List<Antibody> insertTo, List<Antibody> inserted) {
        insertTo.addAll(inserted);
        sortRepertoire(insertTo);
        return new ArrayList<>(insertTo.subList(0, repertoireSize));
    }

    private void replace(List<Antibody> replaced, List<Antibody> replacedWith) {
        sortRepertoire(replaced);
        sortRepertoire(replacedWith);

        int n = Math.min(generatedAntibodies, repertoireSize);
        for (int i = 0; i < n; i++) {
            replaced.add(n - i - 1, replacedWith.get(i));
        }
    }

    private List<Antibody> createNewAntibodies(List<Antibody> repertoire, ExecutorService pool) {
        Map<Antibody, Double> normalizedAffinity = normalizeAffinity(repertoire);

        List<Callable<Antibody>> callables = new ArrayList<>();
        for (Antibody antibody : repertoire) {
            Callable<Antibody> callable = () -> {
                Antibody clone = antibody.cloneAntibody();
                hypermutate(clone, normalizedAffinity.get(antibody));
                clone.affinity = evaluateAntibody(clone);
                return clone;
            };

            callables.add(callable);
        }

        List<Antibody> clones = new ArrayList<>();
        try {
            List<Future<Antibody>> results = pool.invokeAll(callables);

            for (Future<Antibody> result : results) {
                clones.add(result.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return clones;
    }

    private double evaluateAntibody(Antibody antibody) {
        return 1. / ANNTrainer.network.get().evaluate(antibody.values);
    }

    private void hypermutate(Antibody antibody, double normalizedAffinity) {
        double probability = Math.exp(-normalizedAffinity) / ro;

        for (int i = 0; i < antibody.values.length; i++) {
            if (Utils.RANDOM.nextDouble() <= probability) {
                antibody.values[i] += Utils.RANDOM.nextGaussian() * sigma;
            }
        }
    }

    private Map<Antibody, Double> normalizeAffinity(List<Antibody> repertoire) {
        double mean = 0;
        int n = repertoire.size();

        for (int i = 0; i < n; i++) {
            mean += repertoire.get(i).affinity;
        }
        mean /= n;

        double std = 0;
        for (int i = 0; i < n; i++) {
            double diff = repertoire.get(i).affinity - mean;
            std += diff * diff;
        }
        std = Math.sqrt(std / n);

        Map<Antibody, Double> normalized = new HashMap<>();
        for (Antibody antibody : repertoire) {
            normalized.put(antibody, (antibody.affinity - mean) / std);
        }

        return normalized;
    }

    private List<Antibody> select(List<Antibody> repertoire) {
        List<Antibody> selection = new ArrayList<>();

        sortRepertoire(repertoire);
        double lowest = repertoire.get(repertoire.size() - 1).affinity;

        double[][] table = new double[repertoire.size()][numberOfClones];
        for (int i = 0; i < numberOfClones; i++) {
            for (int j = 0, n = repertoire.size(); j < n; j++) {
                table[j][i] = selection.size() / (lowest * (i + 1));
            }
        }

        for (int i = 0; i < numberOfClones; i++) {
            double best = Double.NEGATIVE_INFINITY;
            int position = 0;
            int rank = 0;

            for (int j = 0; j < numberOfClones; j++) {
                for (int k = 0, n = repertoire.size(); k < n; k++) {
                    if (table[k][j] > best) {
                        best = table[k][j];
                        position = k;
                        rank = j;
                    }
                }
            }

            selection.add(repertoire.get(position));
            table[position][rank] = Double.NEGATIVE_INFINITY;
        }

        return selection;
    }

    private void evaluateRepertoire(List<Antibody> repertoire, ExecutorService pool) {
        List<Callable<Void>> callables = new ArrayList<>();
        for (Antibody antibody : repertoire) {
            Callable<Void> evaluation = () -> {
                antibody.affinity = evaluateAntibody(antibody);
                return null;
            };
            callables.add(evaluation);
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
    }

    private List<Antibody> createRepertoire() {
        List<Antibody> repertoire = new ArrayList<>();
        for (int i = 0; i < repertoireSize; i++) {
            repertoire.add(antibodySupplier.get());
        }

        return repertoire;
    }

    private Antibody findBest(List<Antibody> repertoire) {
        sortRepertoire(repertoire);
        return repertoire.get(0);
    }

    private void sortRepertoire(List<Antibody> repertoire) {
        Collections.sort(repertoire, Comparator.reverseOrder());
    }
}
