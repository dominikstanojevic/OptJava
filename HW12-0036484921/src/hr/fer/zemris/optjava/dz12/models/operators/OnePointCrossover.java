package hr.fer.zemris.optjava.dz12.models.operators;

import hr.fer.zemris.optjava.dz12.models.Ant;
import hr.fer.zemris.optjava.dz12.models.Pair;
import hr.fer.zemris.optjava.dz12.Utils;
import hr.fer.zemris.optjava.dz12.models.nodes.Action;
import hr.fer.zemris.optjava.dz12.models.nodes.Function;
import hr.fer.zemris.optjava.dz12.models.nodes.INode;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 12.2.2017..
 */
public class OnePointCrossover {
    private double functionProbability;

    public OnePointCrossover(double functionProbability) {
        this.functionProbability = functionProbability;
    }

    public OnePointCrossover() {
        this(0.9);
    }

    public Pair<Ant, Ant> getChildren(Pair<Ant, Ant> parents) {
        Ant firstChild = new Ant(parents.first);
        Ant secondChild = new Ant(parents.second);

        Random random = ThreadLocalRandom.current();
        Class<?> nodeType = random.nextDouble() <= functionProbability ? Function.class : Action.class;

        int first = random.nextInt(firstChild.program.size());
        while (!nodeType.getClass().isInstance(firstChild.program.get(first).getClass())) {
            first = random.nextInt(firstChild.program.size());
        }

        List<INode> firstSubtree = Utils.copySubtree(firstChild.program, first);
        Utils.removeSubtree(firstChild.program, first);

        int second = random.nextInt(parents.second.program.size());
        while (!nodeType.getClass().isInstance(secondChild.program.get(second).getClass())) {
            second = random.nextInt(secondChild.program.size());
        }

        List<INode> secondSubtree = Utils.copySubtree(secondChild.program, second);
        Utils.removeSubtree(secondChild.program, second);

        Utils.insertSubtree(firstChild.program, first, secondSubtree);
        Utils.insertSubtree(secondChild.program, second, firstSubtree);

        return new Pair<>(firstChild.isValid() ? firstChild : null, secondChild.isValid() ? secondChild : null);
    }
}
