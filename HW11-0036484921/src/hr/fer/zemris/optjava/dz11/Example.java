package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.optjava.dz11.algorithms.IAlgorithm;
import hr.fer.zemris.optjava.dz11.algorithms.ParallelEvaluationGA;
import hr.fer.zemris.optjava.dz11.algorithms.ParallelGenerationGA;
import hr.fer.zemris.optjava.dz11.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz11.crossovers.IntOnePointCrossover;
import hr.fer.zemris.optjava.dz11.evaluators.Evaluator;
import hr.fer.zemris.optjava.dz11.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz11.mutations.IntRandomMutation;
import hr.fer.zemris.optjava.dz11.selections.ISelectionOperator;
import hr.fer.zemris.optjava.dz11.selections.TournamentSelection;
import hr.fer.zemris.optjava.dz11.solutions.GASolution;
import hr.fer.zemris.optjava.dz11.solutions.IntArrayGASolution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * Created by Dominik on 23.1.2017..
 */
public class Example {
    public static final int TOURNAMENT_SIZE = 10;
    public static final double MUTATION_RATE = 0.01;


    public static void main(String[] args) throws IOException {
        if(args.length != 7) {
            System.err.println("Invalid number of arguments. Given: " + args.length + ", expected 7");
            System.exit(-1);
        }

        String inputPath = args[0].trim();
        int numberOfSquares = Integer.parseInt(args[1].trim());
        int populationSize = Integer.parseInt(args[2].trim());
        int maxGenerations = Integer.parseInt(args[3].trim());
        double merr = Double.parseDouble(args[4].trim());
        String paramOutput = args[5].trim();
        String imgOutput = args[6].trim();

        GrayScaleImage template = GrayScaleImage.load(new File(inputPath));

        Evaluator evaluator = new Evaluator(template);

        int size = 1 + 5 * numberOfSquares;
        int[] lowerBounds = new int[size];
        Arrays.fill(lowerBounds, 0);
        int[] upperBounds = new int[size];
        Arrays.fill(upperBounds, 256);
        Supplier<GASolution<int[]>> supplier = () -> new IntArrayGASolution(size, lowerBounds, upperBounds);

        ISelectionOperator<GASolution<int[]>> selection = new TournamentSelection<>(TOURNAMENT_SIZE);
        ICrossoverOperator<GASolution<int[]>> crossover = new IntOnePointCrossover();
        IMutationOperator<GASolution<int[]>> mutation = new IntRandomMutation<>(MUTATION_RATE);

        //ParallelEvaluationGA or ParallelGenerationGA
        IAlgorithm<GASolution<int[]>> ga = new ParallelEvaluationGA<>(populationSize, maxGenerations, merr,
                evaluator, supplier, selection, crossover, mutation);
        GASolution<int[]> solution = ga.run();

        writeSolution(solution, paramOutput);

        evaluator.draw(solution, evaluator.getImage()).save(new File(imgOutput));
    }

    private static void writeSolution(GASolution<int[]> solution, String paramOutput) throws IOException {
        StringJoiner sj = new StringJoiner("\n");
        int[] array = solution.getData();
        for(int number : array) {
            sj.add(Integer.toString(number));
        }

        byte[] bytes = sj.toString().getBytes(StandardCharsets.UTF_8);
        Files.write(Paths.get(paramOutput), bytes);
    }
}
