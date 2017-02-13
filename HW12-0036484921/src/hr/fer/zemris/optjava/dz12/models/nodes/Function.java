package hr.fer.zemris.optjava.dz12.models.nodes;

import hr.fer.zemris.optjava.dz12.models.Grid;

/**
 * Created by Dominik on 5.2.2017..
 */
public enum Function implements INode {
    IF_FOOD_AHEAD {
        @Override
        public int visit(Grid grid, INode[] program, int index) {
            if (grid.isFoodAhead()) {
                index = program[index++].visit(grid, program, index);
                return skipAction(program, index);
            } else {
                index = skipAction(program, index);
                return program[index++].visit(grid, program, index);
            }
        }

        @Override
        public int arity() {
            return 2;
        }
    }, PROG2 {
        @Override
        public int visit(Grid grid, INode[] program, int index) {
            index = program[index++].visit(grid, program, index);
            return program[index++].visit(grid, program, index);
        }

        @Override
        public int arity() {
            return 2;
        }
    }, PROG3 {
        @Override
        public int visit(Grid grid, INode[] program, int index) {
            index = program[index++].visit(grid, program, index);
            index = program[index++].visit(grid, program, index);
            return program[index++].visit(grid, program, index);
        }

        @Override
        public int arity() {
            return 3;
        }
    };

    @Override
    public int depth(INode[] program, MutableInt index) {
        int depth = 0;
        for (int i = 0; i < arity(); i++) {
            int temp = program[index.value++].depth(program, index);
            if (temp > depth) {
                depth = temp;
            }
        }

        return depth + 1;
    }
}
