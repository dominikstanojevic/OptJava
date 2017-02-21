package hr.fer.zemris.optjava.dz12;

import hr.fer.zemris.optjava.dz12.gui.AntTrailGUI;
import hr.fer.zemris.optjava.dz12.models.Ant;
import hr.fer.zemris.optjava.dz12.models.Evaluator;
import hr.fer.zemris.optjava.dz12.models.GP;
import hr.fer.zemris.optjava.dz12.models.Grid;
import hr.fer.zemris.optjava.dz12.models.nodes.Action;
import hr.fer.zemris.optjava.dz12.models.nodes.Function;
import hr.fer.zemris.optjava.dz12.models.nodes.INode;
import hr.fer.zemris.optjava.dz12.models.operators.OnePointCrossover;
import hr.fer.zemris.optjava.dz12.models.operators.OnePointMutation;
import hr.fer.zemris.optjava.dz12.models.operators.TournamentSelection;

import javax.swing.SwingUtilities;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dominik on 12.2.2017..
 */
public class AntTrailGP {
    public static final int PROGRAM_SIZE = 200;
    public static final int MAX_DEPTH = 20;
    public static final int MAX_MOVES = 600;
    public static final double PLAGIARISM = 0.9;
    public static final int TOURNAMENT_SIZE = 7;
    public static final double CROSSOVER_PROBABILITY = 0.85;
    public static final double MUTATION_PROBABILITY = 0.14;
    public static final int MIN_INITIAL_DEPTH = 2;
    public static final int MAX_INITIAL_DEPTH = 6;

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.err.println("Invalid number of arguments. Expected 5, given: " + args.length);
            System.exit(-1);
        }

        String gridFile = args[0].trim();
        int maxGenerations = Integer.parseInt(args[1].trim());
        int populationSize = Integer.parseInt(args[2].trim());
        double minFitness = Double.parseDouble(args[3].trim());
        String outputFile = args[4].trim();

        List<Function> functions = Arrays.asList(Function.values());
        List<Action> terminals = Arrays.asList(Action.values());

        Grid grid = Grid.loadMap(Paths.get(gridFile));
        Evaluator evaluator = new Evaluator(grid, MAX_MOVES, PLAGIARISM);

        OnePointMutation mutation = new OnePointMutation(functions, terminals);
        OnePointCrossover crossover = new OnePointCrossover();
        TournamentSelection selection = new TournamentSelection(TOURNAMENT_SIZE);

        GP gp = new GP(populationSize, maxGenerations, CROSSOVER_PROBABILITY, MUTATION_PROBABILITY, minFitness,
                evaluator, mutation, crossover, selection);

        List<Ant> population = getInitial(functions, terminals, populationSize, MIN_INITIAL_DEPTH, MAX_INITIAL_DEPTH);
        Ant ant = gp.run(population);

        OutputStream os = new FileOutputStream(outputFile);
        Utils.emit(ant.program, 0, os);
        os.close();

        Grid play = Grid.copy(grid);
        play.play(ant, MAX_MOVES);

        Action[] actions = play.getActions();
        SwingUtilities.invokeLater(() -> new AntTrailGUI(grid, actions));
    }

    public static List<Ant> getInitial(
            List<Function> functions, List<Action> terminals, int populationSize, int minDepth, int maxDepth) {
        List<Ant> ants = new ArrayList<>();

        int perDepth = populationSize / (maxDepth - minDepth + 1);
        for (int i = minDepth; i <= maxDepth; i++) {
            int j = 0;
            while (j < perDepth) {
                List<INode> program;
                if (j % 2 == 0) {
                    program = Utils.createTree(functions, terminals, i, Utils.CreationMethod.GROW);
                } else {
                    program = Utils.createTree(functions, terminals, i, Utils.CreationMethod.FULL);
                }

                if (program.size() > PROGRAM_SIZE) {
                    continue;
                }

                ants.add(new Ant(program, MAX_DEPTH, PROGRAM_SIZE));
                j++;
            }
        }

        return ants;
    }
}
