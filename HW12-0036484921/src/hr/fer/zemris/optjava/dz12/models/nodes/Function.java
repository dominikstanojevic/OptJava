package hr.fer.zemris.optjava.dz12.models.nodes;

import hr.fer.zemris.optjava.dz12.models.Grid;
import hr.fer.zemris.optjava.dz12.Utils;

import java.util.List;

/**
 * Created by Dominik on 5.2.2017..
 */
public enum Function implements INode {
    IF_FOOD_AHEAD {
        @Override
        public int visit(Grid grid, List<INode> program, int index) {
            if (grid.isFoodAhead()) {
                index = program.get(index++).visit(grid, program, index);
                return Utils.nextSubtree(program, index);
            } else {
                index = Utils.nextSubtree(program, index);
                return program.get(index++).visit(grid, program, index);
            }
        }

        @Override
        public int arity() {
            return 2;
        }
    }, PROG2 {
        @Override
        public int visit(Grid grid, List<INode> program, int index) {
            index = program.get(index++).visit(grid, program, index);
            return program.get(index++).visit(grid, program, index);
        }

        @Override
        public int arity() {
            return 2;
        }
    }, PROG3 {
        @Override
        public int visit(Grid grid, List<INode> program, int index) {
            index = program.get(index++).visit(grid, program, index);
            index = program.get(index++).visit(grid, program, index);
            return program.get(index++).visit(grid, program, index);
        }

        @Override
        public int arity() {
            return 3;
        }
    };

    @Override
    public int depth(List<INode> program, Utils.MutableInt index) {
        int depth = 0;
        for (int i = 0; i < arity(); i++) {
            int temp = program.get(index.value++).depth(program, index);
            if (temp > depth) {
                depth = temp;
            }
        }

        return depth + 1;
    }
}
