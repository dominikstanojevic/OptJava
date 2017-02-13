package hr.fer.zemris.optjava.dz12.models;

import hr.fer.zemris.optjava.dz12.models.nodes.INode;

import java.util.Arrays;

/**
 * Created by Dominik on 10.2.2017..
 */
public class Ant implements Comparable<Ant> {
    public INode[] program;
    public Integer collected;
    public Double fitness;
    public Ant parent;

    private int maximumDepth;

    public Ant(int length, int maximumDepth) {
        program = new INode[length];
        this.maximumDepth = maximumDepth;
    }

    public Ant(INode[] program, int maximumDepth) {
        int depth = program[0].depth(program, new INode.MutableInt(1));
        if(depth > maximumDepth) {
            throw new IllegalArgumentException("Depth is bigger than maximum.");
        }

        this.program = program;
        this.maximumDepth = maximumDepth;
    }

    public void getNextAction(Grid grid) {
        program[0].visit(grid, program, 1);
    }

    public Ant(Ant parent) {
        this.program = Arrays.copyOf(parent.program, parent.program.length);
        this.parent = parent;
        this.maximumDepth = parent.maximumDepth;
    }

    @Override
    public int compareTo(Ant o) {
        return Double.compare(this.fitness, o.fitness);
    }

    public boolean isValid() {
        boolean valid = checkLength();
        if (!valid) {
            return false;
        }

        return checkDepth();
    }

    private boolean checkDepth() {
        int depth = program[0].depth(program, new INode.MutableInt(1));
        return depth <= maximumDepth;
    }

    private boolean checkLength() {
        for (int i = 0, size = size(), len = 0; i < size; i++) {
            if(program[i] == null) {
                return false;
            }

            len += program[i].arity();
            if (len == i) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        for(int i = 0; i < program.length; i++) {
            if(program[i] == null) {
                return i;
            }
        }

        return program.length;
    }
}
