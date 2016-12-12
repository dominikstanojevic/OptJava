package hr.fer.zemris.optjava.dz7.algorithms;

import hr.fer.zemris.optjava.dz7.ANNTrainer;
import hr.fer.zemris.optjava.dz7.Utils;
import hr.fer.zemris.optjava.dz7.models.Particle;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Dominik on 8.12.2016..
 */
public class PSO {
    private int numberOfParticles;
    private Integer halfNeighborhoodSize;
    private double personalCoefficient;
    private double localCoefficient;
    private double[] maxVelocity;
    private int maxIterations;
    private double minInertiaWeight;
    private double maxInertiaWeight;
    private int dimension;
    private double[] lowerBound;
    private double[] upperBound;
    private double merr;

    public PSO(
            int numberOfParticles, Integer halfNeighborhoodSize, double personalCoefficient, double localCoefficient,
            int maxIterations, double minInertiaWeight, double maxInertiaWeight, int dimension, double[] lowerBound,
            double[] upperBound, double[] maxVelocity, double merr) {

        this.numberOfParticles = numberOfParticles;
        this.halfNeighborhoodSize = halfNeighborhoodSize;
        this.personalCoefficient = personalCoefficient;
        this.localCoefficient = localCoefficient;
        this.maxVelocity = maxVelocity;
        this.maxIterations = maxIterations;
        this.minInertiaWeight = minInertiaWeight;
        this.maxInertiaWeight = maxInertiaWeight;
        this.dimension = dimension;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.merr = merr;
    }

    public PSO(
            int numberOfParticles, double personalCoefficient, double localCoefficient, int maxIterations,
            double minInertiaWeight, double maxInertiaWeight, int dimension, double[] lowerBound, double[] upperBound,
            double[] maxVelocity, double merr) {
        this(numberOfParticles, null, personalCoefficient, localCoefficient, maxIterations, minInertiaWeight,
                maxInertiaWeight, dimension, lowerBound, upperBound, maxVelocity, merr);
    }

    public Particle run() {
        Particle[] particles = initializeParticles();
        if (halfNeighborhoodSize != null) {
            createNeighborhoods(particles);
        }

        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        evaluate(particles, pool);
        Particle globalBest = findGlobalBest(particles);

        for (int i = 1; i <= maxIterations && globalBest.bestCost > merr; i++) {
            evaluate(particles, pool);
            globalBest = findGlobalBest(particles);

            double inertiaWeight = calculateInertiaWeight(i);
            RealVector firstRandom = Utils.createRandomVector(dimension, 0, 1);
            RealVector secondRandom = Utils.createRandomVector(dimension, 0, 1);

            updatePhysics(particles, inertiaWeight, firstRandom, secondRandom, pool, globalBest);

            System.out.println("Iteration: " + i + ", best: " + globalBest);
        }

        pool.shutdown();

        return globalBest;
    }

    private Particle findGlobalBest(Particle[] particles) {
        Particle globalBest = null;
        for (Particle particle : particles) {
            if (globalBest == null || globalBest.cost >= particle.bestCost) {
                globalBest = particle;
            }
        }

        return globalBest;
    }

    private void updatePhysics(
            Particle[] particles, double inertiaWeight, RealVector personalRanodm, RealVector localRandom,
            ExecutorService pool, Particle globalBest) {
        List<Callable<Void>> callables = new ArrayList<>();
        for (Particle particle : particles) {
            callables.add(() -> updatePhysicsForParticle(particle, inertiaWeight, personalRanodm, localRandom,
                    globalBest));
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

    private Void updatePhysicsForParticle(
            Particle particle, double inertiaWeight, RealVector personalRandom, RealVector localRandom,
            Particle globalBest) {
        RealVector inertia = particle.velocity.mapMultiply(inertiaWeight);

        RealVector personalSubtraction = particle.personalBest.subtract(particle.values);
        RealVector personal = personalRandom.ebeMultiply(personalSubtraction).mapMultiply(personalCoefficient);

        RealVector localSubtraction;
        if (halfNeighborhoodSize != null) {
            localSubtraction = particle.neighborhood.localBest.subtract(particle.values);
        } else {
            localSubtraction = globalBest.personalBest.subtract(particle.values);
        }

        RealVector local = localRandom.ebeMultiply(localSubtraction).mapMultiplyToSelf(localCoefficient);

        particle.velocity = checkVelocityBounds(inertia.add(personal).add(local));

        particle.values = particle.values.add(particle.velocity);
        return null;
    }

    private RealVector checkVelocityBounds(RealVector velocity) {
        double[] v = velocity.toArray();
        for (int i = 0; i < dimension; i++) {
            if (v[i] < -maxVelocity[i]) {
                v[i] = -maxVelocity[i];
            } else if (v[i] > maxVelocity[i]) {
                v[i] = maxVelocity[i];
            }
        }

        return new ArrayRealVector(v);
    }

    private double calculateInertiaWeight(int iteration) {
        double factor = ((double) iteration) / maxIterations;
        return factor * (minInertiaWeight - maxInertiaWeight) + maxInertiaWeight;
    }

    private void evaluate(Particle[] particles, ExecutorService pool) {
        List<Callable<Void>> callables = new ArrayList<>();
        for (Particle particle : particles) {
            callables.add(() -> train(particle));
        }

        try {
            List<Future<Void>> results = pool.invokeAll(callables);

            for (Future<Void> result : results) {
                result.get();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Jebiga");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(halfNeighborhoodSize != null) {
            for (Particle particle : particles) {
                particle.neighborhood.updateLocalBest();
            }
        }
    }

    private Void train(Particle particle) {
        particle.cost = ANNTrainer.network.get().evaluate(particle.values.toArray());
        particle.updateBest();
        return null;
    }

    private void createNeighborhoods(Particle[] particles) {
        if (halfNeighborhoodSize == null) {
            for (Particle particle : particles) {
                particle.setNeighborhood(particles);
            }
        } else {
            for (int i = 0; i < numberOfParticles; i++) {
                Particle[] neighbors = new Particle[2 * halfNeighborhoodSize];
                for (int j = -halfNeighborhoodSize; j < halfNeighborhoodSize; j++) {
                    int modulo = (i + j) % numberOfParticles;
                    if (modulo < 0) {
                        modulo = numberOfParticles + modulo;
                    }
                    neighbors[j + halfNeighborhoodSize] = particles[modulo];
                }

                particles[i].setNeighborhood(neighbors);
            }
        }
    }

    private Particle[] initializeParticles() {
        Particle[] particles = new Particle[numberOfParticles];
        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = new Particle(dimension, lowerBound, upperBound, maxVelocity);
        }

        return particles;
    }
}
