package hr.fer.zemris.optjava.dz4.part2.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dominik on 4.11.2016..
 */
public class Bin implements Iterable<Stick> {
    private int maxHeight;
    private int height;
    private List<Stick> sticks = new ArrayList<>();

    public Bin(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public boolean addStick(Stick stick) {
        int stickHeight = stick.getHeight();

        if (stickHeight + height > maxHeight) {
            return false;
        }

        sticks.add(stick);
        height += stickHeight;
        return true;
    }

    @Override
    public Iterator<Stick> iterator() {
        return sticks.iterator();
    }

    public boolean contains(Stick stick) {
        return sticks.contains(stick);
    }
}
