package hr.fer.zemris.optjava.dz5.models.cfplans;

/**
 * Created by Dominik on 11.11.2016..
 */
public class LogCompFactorPlan implements ICompFactorPlan {
    private double base;

    public LogCompFactorPlan(int maxIterations, double factor) {
        base = maxIterations * factor;
    }

    @Override
    public double getCF(int iteration) {
        double cf = Math.max(0, Math.log(iteration) / Math.log(base));
        return Math.min(cf, 1);
    }
}
