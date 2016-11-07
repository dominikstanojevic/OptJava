package hr.fer.zemris.optjava.dz4.part2.models;

import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 4.11.2016..
 */
public class BinContainer extends AbstractSolution<List<Bin>> {

    public BinContainer() {
        this(new ArrayList<>());
    }

    protected BinContainer(List<Bin> chromosome) {
        super(chromosome);
    }

    public BinContainer duplicate() {
        List<Bin> copyChromosome = chromosome.stream().map(Bin::duplicate).collect(Collectors.toList());

        return new BinContainer(copyChromosome);
    }

    public int size() {
        return chromosome.size();
    }

    @Override
    public void randomize(Random random) {
        throw new RuntimeException("Operation using Random object is not supported.");
    }

    public void randomize(List<Stick> sticks) {
        Collections.shuffle(sticks);

        for (Stick stick : sticks) {
            Bin bin = new Bin();
            bin.addStick(stick);
            chromosome.add(bin);
        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (Bin bin : chromosome) {
            sj.add(bin.toString());
        }

        return sj.toString();
    }

    public List<Stick> rearrange(List<Stick> unassignedBefore) {
        List<Stick> unassignedAfter = new ArrayList<>();

        for (Stick stick : unassignedBefore) {
            boolean added = false;
            for (Bin bin : chromosome) {
                List<Stick> replaced = getReplaced(bin, stick);
                if (replaced != null) {
                    unassignedAfter.addAll(replaced);
                    added = true;
                    break;
                }
            }

            if (!added) {
                unassignedAfter.add(stick);
            }
        }

        return unassignedAfter;
    }

    private List<Stick> getReplaced(Bin bin, Stick stick) {
        List<Stick> taken = new ArrayList<>();
        int total = 0;

        int finish = Math.min(3, bin.size());
        for (int i = 0; i < finish; i++) {
            Stick biggest = bin.findBiggest();
            bin.removeStick(biggest);
            taken.add(biggest);
            total += biggest.getHeight();

            if ((stick.getHeight() > total) && (bin.getCurrentHeight() + stick.getHeight() <= Bin.maxHeight)) {
                bin.addStick(stick);
                return taken;
            }
        }

        bin.addSticks(taken);

        return null;
    }

    public void addStick(List<Stick> unassigned) {
        Collections.sort(unassigned, Collections.reverseOrder());
        initialize(unassigned);
    }

    private void initialize(List<Stick> sticks) {
        for (Stick stick : sticks) {
            Optional<Bin> bin =
                    chromosome.stream().filter(b -> b.getCurrentHeight() + stick.getHeight() <= Bin.maxHeight)
                            .findFirst();

            if (bin.isPresent()) {
                bin.get().addStick(stick);
            } else {
                Bin newBin = new Bin();
                newBin.addStick(stick);
                chromosome.add(newBin);
            }
        }
    }

    public int numberOfSticks() {
        return chromosome.stream().mapToInt(b -> b.size()).sum();
    }
}
