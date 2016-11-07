package hr.fer.zemris.optjava.dz4.part2.models;

import hr.fer.zemris.optjava.dz4.models.Pair;
import hr.fer.zemris.optjava.dz4.models.crossovers.ICrossoverOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public class BinCrossoverOperator implements ICrossoverOperator<BinContainer> {

    @Override
    public Pair<BinContainer, BinContainer> getChildren(
            Pair<BinContainer, BinContainer> parents, Random random) {
        BinContainer firstClone = parents.first.duplicate();
        BinContainer secondClone = parents.second.duplicate();

        Pair<Integer, Integer> firstPoints = getPoints(random, firstClone.size());
        List<Bin> firstTransferring =
                new ArrayList<>(firstClone.chromosome.subList(firstPoints.first, firstPoints.second));

        Pair<Integer, Integer> secondPoints = getPoints(random, secondClone.size());
        List<Bin> secondTransferring =
                new ArrayList<>(secondClone.chromosome.subList(secondPoints.first, secondPoints.second));

        copyBinsToContainer(secondClone, firstTransferring, secondPoints.first);
        copyBinsToContainer(firstClone, secondTransferring, firstPoints.second);

        List<Stick> firstUnassigned = removeBinWithDuplicates(firstClone, secondTransferring);
        List<Stick> secondUnassigned = removeBinWithDuplicates(secondClone, firstTransferring);

        List<Stick> firstAfter = firstClone.rearrange(firstUnassigned);
        List<Stick> secondAfter = secondClone.rearrange(secondUnassigned);

        firstClone.addStick(firstAfter);
        secondClone.addStick(secondAfter);

        return new Pair<>(firstClone, secondClone);
    }

    private void copyBinsToContainer(BinContainer container, List<Bin> bins, int fromIndex) {
        int size = bins.size();
        for (int i = 0; i < size; i++) {
            container.chromosome.add(fromIndex + i, bins.get(i));
        }
    }

    private Pair<Integer, Integer> getPoints(Random random, int bound) {
        int firstPoint = random.nextInt(bound);
        int secondPoint = random.nextInt(bound);

        return new Pair<>(Math.min(firstPoint, secondPoint), Math.max(firstPoint, secondPoint));
    }

    private List<Stick> removeBinWithDuplicates(BinContainer container, List<Bin> bins) {
        List<Stick> unassigned = new ArrayList<>();

        for (Bin bin : bins) {
            for (Stick stick : bin) {
                Optional<Bin> removed =
                        container.chromosome.stream().filter(b -> !b.equals(bin) && b.contains(stick)).findFirst();

                container.chromosome.remove(removed.get());
                addUnassigned(unassigned, bins, removed.get());

            }
        }

        return unassigned;
    }

    private void addUnassigned(List<Stick> unassigned, List<Bin> bins, Bin removed) {
        for (Stick stick : removed) {
            boolean contains = bins.stream().anyMatch(b -> b.contains(stick));
            if (!contains) {
                unassigned.add(stick);
            }
        }
    }
}
