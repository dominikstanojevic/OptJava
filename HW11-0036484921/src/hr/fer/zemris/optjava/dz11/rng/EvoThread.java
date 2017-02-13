package hr.fer.zemris.optjava.dz11.rng;

/**
 * Created by Dominik on 20.1.2017..
 */
public class EvoThread extends Thread implements IRNGProvider {
    private IRNG random = new RNGRandomImpl();

    public EvoThread() {
    }

    public EvoThread(Runnable target) {
        super(target);
    }

    public EvoThread(String name) {
        super(name);
    }

    public EvoThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public EvoThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public EvoThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public EvoThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public IRNG getRNG() {
        return random;
    }
}
