package hr.fer.zemris.optjava.dz12.models.nodes;

import hr.fer.zemris.optjava.dz12.models.Ant;
import hr.fer.zemris.optjava.dz12.models.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static hr.fer.zemris.optjava.dz12.models.nodes.INode.CreationMethod.GROW;

/**
 * Created by Dominik on 5.2.2017..
 */
public interface INode {
    int visit(Grid grid, INode[] program, int index);

    int arity();

    int depth(INode program[], MutableInt index);

    class MutableInt {
        public int value;

        public MutableInt(int value) {
            this.value = value;
        }
    }

    default int skipAction(INode[] program, int index) {
        for (int counter = 0;
             counter > -1 && (index < program.length && program[index] != null);
             counter += program[index++].arity() - 1)
            ;
        return index;
    }

    enum CreationMethod {
        GROW, FULL
    }

    static List<INode> createTree(
            List<INode> functions, List<INode> terminals, int depth, CreationMethod method) {
        double ratio = terminals.size() / (terminals.size() + functions.size());
        Random random = ThreadLocalRandom.current();

        INode node;
        List<INode> expression = new ArrayList<>();
        if (depth == 0 || (method == GROW && random.nextDouble() < ratio)) {
            node = terminals.get(random.nextInt(terminals.size()));
            expression.add(node);
        } else {
            node = functions.get(random.nextInt(functions.size()));

            int len = node.arity();
            expression.add(node);
            for (int i = 0; i < len; i++) {
                List<INode> child = createTree(functions, terminals, depth - 1, method);
                expression.addAll(child);
            }
        }

        return expression;
    }
}
