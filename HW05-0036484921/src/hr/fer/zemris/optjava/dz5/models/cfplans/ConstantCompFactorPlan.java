package hr.fer.zemris.optjava.dz5.models.cfplans;

/**
 * Created by Dominik on 11.11.2016..
 */
public class ConstantCompFactorPlan implements ICompFactorPlan {
    private double cf;

    public ConstantCompFactorPlan(double cf) {
        this.cf = cf;
    }

    @Override
    public double getCF(int iteration) {
        return cf;
    }
}
