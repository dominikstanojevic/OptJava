package hr.fer.zemris.optjava.dz5.models.cfplans;

/**
 * Created by Dominik on 14.11.2016..
 */
public class LinearCompFactorPlan implements ICompFactorPlan {
    private double increment;
    private double current = 0;

    public LinearCompFactorPlan(int maxIterations) {
       increment = 1. / maxIterations;
    }

    @Override
    public double getCF(int iteration) {
        double curr = current;
        current += increment;

        return curr;
    }
}
