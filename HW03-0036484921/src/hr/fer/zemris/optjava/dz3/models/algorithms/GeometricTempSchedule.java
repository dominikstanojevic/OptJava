package hr.fer.zemris.optjava.dz3.models.algorithms;

/**
 * Created by Dominik on 21.10.2016..
 */
public class GeometricTempSchedule implements ITempSchedule {
    private double alpha;
    private double tInitial;
    private double tCurrent;
    private int innerLimit;
    private int outerLimit;

    private static final double THRESHOLD = 0.1;

    public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimit) {
        this.alpha = alpha;
        this.tInitial = tInitial;
        this.tCurrent = tInitial;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimit;
    }

    public GeometricTempSchedule(double tInitial, int innerLimit, int outerLimit) {
        this.tInitial = tInitial;
        this.tCurrent = tInitial;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimit;

        alpha = calculateAlpha(tInitial, outerLimit);
    }

    private double calculateAlpha(double tInitial, int outerLimit) {
        return Math.pow(Math.E, Math.log(THRESHOLD / tInitial)/outerLimit);
    }

    @Override
    public double getNextTemperature() {
        double current = tCurrent;
        tCurrent *= alpha;
        return current;
    }

    @Override
    public int getInnerLoopCounter() {
       return innerLimit;
    }

    @Override
    public int getOuterLoopCounter() {
        return outerLimit;
    }
}
