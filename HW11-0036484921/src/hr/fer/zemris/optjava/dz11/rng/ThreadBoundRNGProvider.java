package hr.fer.zemris.optjava.dz11.rng;

/**
 * Created by Dominik on 20.1.2017..
 */
public class ThreadBoundRNGProvider implements IRNGProvider {
    @Override
    public IRNG getRNG() {
        Thread current = Thread.currentThread();
        if (current instanceof EvoThread) {
            return ((EvoThread) current).getRNG();
        }

        throw new RuntimeException("Trying to get thread bound random from thread which is not EvoThread.");
    }
}
