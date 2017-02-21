package hr.fer.zemris.optjava.dz12.models;

import hr.fer.zemris.optjava.dz12.Utils;
import hr.fer.zemris.optjava.dz12.models.nodes.INode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 10.2.2017..
 */
public class Ant implements Comparable<Ant> {
    public List<INode> program;
    public Integer collected;
    public Double fitness;
    public Ant parent;

    private int maximumDepth;
    private int maxSize;

    public Ant(List<INode> program, int maximumDepth, int maxSize) {
        this.program = program;
        this.maximumDepth = maximumDepth;
        this.maxSize = maxSize;
    }

    public void getNextAction(Grid grid) {
        program.get(0).visit(grid, program, 1);
    }

    public Ant(Ant parent) {
        this.program = new ArrayList<>(parent.program);
        this.parent = parent;
        this.maximumDepth = parent.maximumDepth;
        this.maxSize = parent.maxSize;
    }

    @Override
    public int compareTo(Ant o) {
        return Double.compare(this.fitness, o.fitness);
    }

    public boolean isValid() {
        if(program.size() > maxSize || !checkDepth()) {
            return false;
        }

        return true;
    }

    private boolean checkDepth() {
        return program.get(0).depth(program, new Utils.MutableInt(1)) <= maximumDepth;
    }
}
