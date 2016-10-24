package hr.fer.zemris.optjava.dz3.models.neighborhoods;

/**
 * Created by Dominik on 18.10.2016..
 */
public interface INeighborhood<T> {
    T randomNeighbor(T solution);
}
