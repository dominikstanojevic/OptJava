package hr.fer.zemris.optjava.dz3.models.algorithms;

/**
 * Created by Dominik on 21.10.2016..
 */
public interface ITempSchedule {
    double getNextTemperature();
    int getInnerLoopCounter();
    int getOuterLoopCounter();
}
