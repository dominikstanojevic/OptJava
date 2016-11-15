package hr.fer.zemris.optjava.dz4.part2.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created by Dominik on 4.11.2016..
 */
public class Bin implements Iterable<Stick> {
    public static int maxHeight = 20;
    private int currentHeight;
    private List<Stick> sticks = new ArrayList<>();

    public Bin() {
    }

    public Bin duplicate() {
        Bin copy = new Bin();
        copy.currentHeight = currentHeight;
        copy.sticks = new ArrayList<>(sticks);

        return copy;
    }

    public boolean addStick(Stick stick) {
        int stickHeight = stick.getHeight();

        if (stickHeight + currentHeight > maxHeight) {
            return false;
        }

        sticks.add(stick);
        currentHeight += stickHeight;
        return true;
    }

    @Override
    public Iterator<Stick> iterator() {
        return sticks.iterator();
    }

    public boolean contains(Stick stick) {
        return sticks.contains(stick);
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    public Stick findBiggest() {
        Optional<Stick> stick = sticks.stream().max(Comparator.naturalOrder());
        return stick.isPresent() ? stick.get() : null;
    }

    public void removeStick(Stick stick) {
        sticks.remove(stick);
        currentHeight -= stick.getHeight();
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for(Stick stick : sticks) {
            sj.add(stick.toString());
        }

        return sj.toString();
    }

    public int size() {
        return sticks.size();
    }

    public void addSticks(Collection<Stick> sticks) {
        sticks.forEach(this::addStick);
    }
}
