package hr.fer.zemris.optjava.dz12.models.nodes;

import hr.fer.zemris.optjava.dz12.models.Grid;
import hr.fer.zemris.optjava.dz12.Utils;

import java.util.List;

/**
 * Created by Dominik on 5.2.2017..
 */
public interface INode {
    int visit(Grid grid, List<INode> program, int index);

    int arity();

    int depth(List<INode> program, Utils.MutableInt index);
}
