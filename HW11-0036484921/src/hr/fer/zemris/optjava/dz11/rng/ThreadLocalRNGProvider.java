package hr.fer.zemris.optjava.dz11.rng;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 20.1.2017..
 */
public class ThreadLocalRNGProvider implements IRNGProvider {
    private ThreadLocal<IRNG> threadLocal = ThreadLocal.withInitial(RNGRandomImpl::new);

    @Override
    public IRNG getRNG() {
        return threadLocal.get();
    }
}
