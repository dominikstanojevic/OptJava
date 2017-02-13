package hr.fer.zemris.optjava.dz11.rng;

/**
 * Created by Dominik on 20.1.2017..
 */
public interface IRNG {
    double nextDouble();
    double nextDouble(double min, double max);
    float nextFloat();
    float nextFloat(float min, float max);
    int nextInt();
    int nextInt(int min, int max);
    boolean nextBoolean();
    double nextGaussian();
}
