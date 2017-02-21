package hr.fer.zemris.optjava.dz12.models.operators;

import hr.fer.zemris.optjava.dz12.models.Ant;
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
public class OnePointMutation {
    private List<Function> functions;
    private List<Action> terminals;

    public OnePointMutation(List<Function> functions, List<Action> terminals) {
        this.functions = functions;
        this.terminals = terminals;
    }

    public Ant mutate(Ant ant) {
        Ant mutated = new Ant(ant);

        Random random = ThreadLocalRandom.current();

        int start = random.nextInt(mutated.program.size());
        int maxDepth = mutated.program.get(start).depth(mutated.program, new Utils.MutableInt(start + 1));

        Utils.removeSubtree(mutated.program, start);

        int depth = random.nextInt(maxDepth + 1);
        Utils.CreationMethod method = random.nextBoolean() ? Utils.CreationMethod.GROW : Utils.CreationMethod.FULL;
        List<INode> subtree = Utils.createTree(functions, terminals, depth, method);
        Utils.insertSubtree(mutated.program, start, subtree);

        return mutated.isValid() ? mutated : null;
    }
}
