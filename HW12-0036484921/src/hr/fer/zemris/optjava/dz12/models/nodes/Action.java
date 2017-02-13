package hr.fer.zemris.optjava.dz12.models.nodes;

import hr.fer.zemris.optjava.dz12.models.Grid;

/**
 * Created by Dominik on 5.2.2017..
 */
public enum Action implements INode {
    MOVE,
    RIGHT,
    LEFT;

    @Override
    public int visit(Grid grid, INode[] program, int index) {
        grid.executeAction(this);
        return index;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public int depth(INode[] program, MutableInt index) {
        return 0;
    }
}
