package hr.fer.zemris.optjava.dz4.part2.models;

/**
 * Created by Dominik on 4.11.2016..
 */
public class Stick {
    private int height;

    public Stick(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return Integer.toString(height);
    }
}
