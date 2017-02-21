package hr.fer.zemris.optjava.dz12;

import hr.fer.zemris.optjava.dz12.models.nodes.Action;
import hr.fer.zemris.optjava.dz12.models.nodes.Function;
import hr.fer.zemris.optjava.dz12.models.nodes.INode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static hr.fer.zemris.optjava.dz12.Utils.CreationMethod.GROW;

/**
 * Created by Dominik on 14.2.2017..
 */
public class Utils {
    public static int nextSubtree(List<INode> program, int index) {
        for (int counter = 0; counter > -1; counter += program.get(index++).arity() - 1)
            ;
        return index;
    }

    public static List<INode> copySubtree(List<INode> program, int index) {
        return new ArrayList<>(getSubtree(program, index));
    }

    private static List<INode> getSubtree(List<INode> program, int index) {
        int end = nextSubtree(program, index);
        return program.subList(index, end);
    }

    public static void removeSubtree(List<INode> program, int index) {
        getSubtree(program, index).clear();
    }

    public static void insertSubtree(List<INode> program, int position, List<INode> subtree) {
        program.addAll(position, subtree);
    }

    public static class MutableInt {
        public int value;

        public MutableInt(int value) {
            this.value = value;
        }
    }

    public enum CreationMethod {
        GROW, FULL
    }

    public static List<INode> createTree(
            List<Function> functions, List<Action> terminals, int depth, CreationMethod method) {
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

    public static int emit(List<INode> program, int index, OutputStream os) throws IOException {
        INode current = program.get(index++);
        os.write(current.toString().getBytes(StandardCharsets.UTF_8));

        if (current.arity() > 0) {
            os.write((int) '(');

            for (int i = 0; i < current.arity(); i++) {
                index = emit(program, index, os);
            }

            os.write((int) ')');
        }

        os.flush();
        return index;
    }
}
