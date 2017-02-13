package hr.fer.zemris.optjava.dz12.models.operators;

import hr.fer.zemris.optjava.dz12.models.Ant;
import hr.fer.zemris.optjava.dz12.models.nodes.Action;
import hr.fer.zemris.optjava.dz12.models.nodes.Function;
import hr.fer.zemris.optjava.dz12.models.nodes.INode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 12.2.2017..
 */
public class OnePointMutation {
    private List<INode> nodes;

    public OnePointMutation(List<INode> nodes) {
        this.nodes = nodes;
    }

    public Ant mutate(Ant ant) {
        Ant mutated = new Ant(ant);
        int length = mutated.size();

        int index = ThreadLocalRandom.current().nextInt(length);
        int next = mutated.program[index].skipAction(mutated.program, index);

        int depth = mutated.program[index].depth(mutated.program, new INode.MutableInt(index + 1));

        List<INode> listSubtree =
                INode.createTree(Arrays.asList(Function.values()), Arrays.asList(Action.values()), depth,
                        INode.CreationMethod.GROW);
        INode[] subtree = listSubtree.toArray(new INode[listSubtree.size()]);
        int diff = subtree.length - (next - index);

        int total = mutated.program.length - (next + Math.abs(diff));
        if (total < 0) {
            return null;
        }

        System.arraycopy(ant.program, next, mutated.program, next + diff, total);
        System.arraycopy(subtree, 0, mutated.program, index, subtree.length);

        return mutated.isValid() ? mutated : null;
    }
}
