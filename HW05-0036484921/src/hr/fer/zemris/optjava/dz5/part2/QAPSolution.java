package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.models.solutions.AbstractSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 12.11.2016..
 */
public class QAPSolution extends AbstractSolution<int[]> {
    private Integer hashCode;

    public QAPSolution(int size) {
        super(new int[size]);
    }

    @Override
    public void randomize(Random random) {
        int size = chromosome.length;

        List<Integer> unused = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            unused.add(i);
        }

        for (int i = 0; i < size; i++) {
            chromosome[i] = unused.remove(random.nextInt(unused.size()));
        }


    }

    @Override
    public String toString() {
        List<Integer> list = Arrays.stream(chromosome).boxed().collect(Collectors.toList());

        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (int i = 0; i < chromosome.length; i++) {
            sj.add(Integer.toString(list.indexOf(i) + 1));
        }

        return sj.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QAPSolution that = (QAPSolution) o;
        if(this.hashCode() != that.hashCode()) {
            return false;
        }

        return Arrays.equals(this.chromosome, that.chromosome);
    }

    @Override
    public int hashCode() {
        if(hashCode == null) {
            hashCode = super.hashCode();
        }

        return hashCode;
    }
}
