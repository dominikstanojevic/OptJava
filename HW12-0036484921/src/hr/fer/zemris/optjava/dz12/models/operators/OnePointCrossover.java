package hr.fer.zemris.optjava.dz12.models.operators;

import hr.fer.zemris.optjava.dz12.models.Ant;
import hr.fer.zemris.optjava.dz12.models.Pair;
import hr.fer.zemris.optjava.dz12.models.nodes.INode;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 12.2.2017..
 */
public class OnePointCrossover {
    public Pair<Ant, Ant> getChildren(Pair<Ant, Ant> parents) {
        Ant firstChild = new Ant(parents.first);
        Ant secondChild = new Ant(parents.second);

        INode[] firstProgram = firstChild.program;
        INode[] secondProgram = secondChild.program;

        Random random = ThreadLocalRandom.current();

        int startFirst = random.nextInt(firstChild.size());
        int startSecond = random.nextInt(secondChild.size());

        int endFirst = firstProgram[startFirst].skipAction(firstProgram, startFirst);
        int endSecond = secondProgram[startSecond].skipAction(secondProgram, startSecond);

        int lenFirst = endFirst - startFirst;
        int lenSecond = endSecond - startSecond;
        int diff = lenSecond - lenFirst;

        int firstLength = firstProgram.length - (endFirst + diff);
        int secondLength = secondProgram.length - (endSecond - diff);
        if(firstLength < 0 || secondLength < 0) {
            return new Pair<>(null, null);
        }

        System.arraycopy(parents.first.program, startFirst, firstProgram, endFirst + diff, firstLength);
        System.arraycopy(parents.second.program, startSecond, secondProgram, endSecond - diff, secondLength);

        System.arraycopy(parents.second.program, startSecond, firstProgram, startFirst, lenSecond);
        System.arraycopy(parents.first.program, startFirst, secondProgram, startSecond, lenFirst);

        return new Pair<>(firstChild.isValid() ? firstChild : null, secondChild.isValid() ? secondChild : null);
    }
}
