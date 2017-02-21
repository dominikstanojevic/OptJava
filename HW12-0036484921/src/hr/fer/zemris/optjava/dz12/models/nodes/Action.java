package hr.fer.zemris.optjava.dz12.models.nodes;

import hr.fer.zemris.optjava.dz12.models.Grid;
import hr.fer.zemris.optjava.dz12.Utils;

import java.util.List;

/**
 * Created by Dominik on 5.2.2017..
 */
public enum Action implements INode {
    MOVE,
    RIGHT,
    LEFT;

    @Override
    public int visit(Grid grid, List<INode> program, int index) {
        grid.executeAction(this);
        return index;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public int depth(List<INode> program, Utils.MutableInt index) {
        return 0;
    }
}
