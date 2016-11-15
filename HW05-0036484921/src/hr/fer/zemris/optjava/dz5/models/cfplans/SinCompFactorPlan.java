package hr.fer.zemris.optjava.dz5.models.cfplans;

/**
 * Created by Dominik on 14.11.2016..
 */
public class SinCompFactorPlan implements ICompFactorPlan {
    private double factor;

    public SinCompFactorPlan(int maxIterations) {
        factor = Math.PI / (2 * maxIterations);
    }

    @Override
    public double getCF(int iteration) {
        return Math.sin(factor * iteration);
    }
}
