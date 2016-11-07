package hr.fer.zemris.optjava.dz4.part2.models;

import java.util.Objects;

/**
 * Created by Dominik on 4.11.2016..
 */
public class Stick implements Comparable<Stick> {
    private static int idCounter;

    private int id;
    private int height;

    public Stick(int height) {
        this.height = height;
        id = idCounter;
        idCounter++;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    @Override
    public int compareTo(Stick o) {
        Objects.requireNonNull(o, "Cannot compare to null.");

        return Integer.compare(this.height, o.height);
    }
}
