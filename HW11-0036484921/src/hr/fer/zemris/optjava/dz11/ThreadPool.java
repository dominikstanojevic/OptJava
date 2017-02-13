package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.optjava.dz11.rng.EvoThread;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Dominik on 20.1.2017..
 */
public class ThreadPool<T> {
    private Thread[] threads;

    private BlockingQueue<Callable<T>> jobs = new LinkedBlockingQueue<>();
    private BlockingQueue<Future<T>> results = new LinkedBlockingQueue<>();

    private final Callable<T> RED_PILL = () -> null;

    public ThreadPool(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Invalid number of arguments.");
        }

        threads = new EvoThread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new EvoThread(() -> {
                while (true) {
                    try {

                        Callable<T> job = jobs.take();
                        if (job == RED_PILL) {
                            break;
                        }

                        T result = job.call();
                        results.put(CompletableFuture.completedFuture(result));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }
    }

    public void addJob(Callable<T> job) {
        Objects.requireNonNull(job, "Job cannot be null.");

        try {
            jobs.put(job);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Future<T> getResult() {
        try {
            return results.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void shutdown() {
        for (int i = 0; i < threads.length; i++) {
            jobs.add(RED_PILL);
        }
    }

    public int poolSize() {
        return threads.length;
    }
}