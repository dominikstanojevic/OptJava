package hr.fer.zemris.optjava.dz7.models;

import hr.fer.zemris.optjava.dz7.Utils;
import hr.fer.zemris.optjava.dz7.models.neural.Weights;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 8.12.2016..
 */
public class Particle implements Weights {
    public RealVector values;
    public double cost;

    public RealVector personalBest;
    public double bestCost;

    public RealVector velocity;

    public Neighborhood neighborhood;

    public Particle(int size, double[] lowerBound, double[] upperBound, double[] maxVelocity) {
        values = new ArrayRealVector(size);
        velocity = new ArrayRealVector(size);

        randomize(size, lowerBound, upperBound, maxVelocity);
    }

    private void randomize(int size, double[] lowerBound, double[] upperBound, double[] maxVelocity) {
        for (int i = 0; i < size; i++) {
            values.setEntry(i, Utils.RANDOM.nextDouble() * (upperBound[i] - lowerBound[i]) + lowerBound[i]);
            velocity.setEntry(i, Utils.RANDOM.nextDouble() * 2 * maxVelocity[i] - maxVelocity[i]);
        }
    }

    public void updateBest() {
        if (personalBest == null || cost <= bestCost) {
            personalBest = values;
            bestCost = cost;
        }
    }

    @Override
    public String toString() {
        return Double.toString(bestCost);
    }

    public void setNeighborhood(Particle[] neighbors) {
        neighborhood = new Neighborhood(neighbors);
    }

    @Override
    public int numberOfWeights() {
        return values.getDimension();
    }

    @Override
    public double[] getWeights() {
        return personalBest.toArray();
    }

    public class Neighborhood {
        private Particle[] neighbors;

        public RealVector localBest;
        public double cost;

        public Neighborhood(Particle[] neighbors) {
            this.neighbors = neighbors;
        }

        public void updateLocalBest() {
            if (localBest == null || cost >= Particle.this.bestCost) {
                localBest = personalBest;
                cost = Particle.this.bestCost;
            }

            for (Particle neighbor : neighbors) {
                if (cost >= neighbor.bestCost) {
                    localBest = neighbor.personalBest;
                    cost = neighbor.bestCost;
                }
            }
        }
    }
}
