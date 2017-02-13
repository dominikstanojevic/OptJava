package hr.fer.zemris.optjava.dz12.models;

import hr.fer.zemris.optjava.dz12.models.nodes.Action;
import hr.fer.zemris.optjava.dz12.models.nodes.Function;
import hr.fer.zemris.optjava.dz12.models.nodes.INode;
import hr.fer.zemris.optjava.dz12.models.operators.OnePointCrossover;
import hr.fer.zemris.optjava.dz12.models.operators.OnePointMutation;
import hr.fer.zemris.optjava.dz12.models.operators.TournamentSelection;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Dominik on 12.2.2017..
 */
public class Test {
    public static void main(String[] args) throws IOException {
        List<INode> nodes = getNodes();

        java.util.function.Function<INode[], Ant> antSupplier = program -> {
            if (program.length > 200 || program[0].depth(program, new INode.MutableInt(1)) > 20) {
                return null;
            }

            return new Ant(program, 20);
        };
        Grid grid = Grid.loadMap(Paths.get(
                "D:\\fer\\5. " + "semestar\\ROPAERUJ\\workspace\\HW12-0036484921\\maps\\13-SantaFeAntTrail.txt"));
        Evaluator evaluator = new Evaluator(grid, 600, 0.9);

        OnePointMutation mutation = new OnePointMutation(nodes);
        OnePointCrossover crossover = new OnePointCrossover();
        TournamentSelection selection = new TournamentSelection(7);

        GP gp = new GP(500, 500, antSupplier, 0.84, 0.15, 0.01, evaluator, mutation, crossover, selection);

        List<Ant> population = getInitial(Arrays.asList(Function.values()), Arrays.asList(Action.values()));
        Ant ant = gp.run(population);

        System.out.println(grid.play(ant, 600));
    }

    public static List<INode> getNodes() {
        List<INode> nodes = new ArrayList<>();
        nodes.addAll(Arrays.asList(Function.values()));
        nodes.addAll(Arrays.asList(Action.values()));
        Collections.shuffle(nodes);
        return nodes;
    }

    public static List<Ant> getInitial(List<INode> functions, List<INode> terminals) {
        List<Ant> ants = new ArrayList<>();

        int petDepth = 500 / 5;
        for (int i = 2; i <= 6; i++) {
            int j = 0;
            while (j < petDepth) {
                INode[] program;
                if (j % 2 == 0) {
                    List<INode> prog = INode.createTree(functions, terminals, i, INode.CreationMethod.GROW);
                    program = prog.toArray(new INode[200]);
                } else {
                    List<INode> prog = INode.createTree(functions, terminals, i, INode.CreationMethod.FULL);
                    program = prog.toArray(new INode[200]);
                }

                if (program.length > 200 || program[0].depth(program, new INode.MutableInt(1)) > 20) {
                   continue;
                }

                ants.add(new Ant(program, 20));
                j++;
            }
        }

        return ants;
    }
}
