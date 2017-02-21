package hr.fer.zemris.optjava.dz4.part2;

import hr.fer.zemris.optjava.dz4.models.IFunction;
import hr.fer.zemris.optjava.dz4.models.algorithms.SteadyStateGA;
import hr.fer.zemris.optjava.dz4.models.crossovers.ICrossoverOperator;
import hr.fer.zemris.optjava.dz4.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz4.models.mutations.IMutationOperator;
import hr.fer.zemris.optjava.dz4.models.selections.ISelection;
import hr.fer.zemris.optjava.dz4.models.selections.TournamentSelection;
import hr.fer.zemris.optjava.dz4.part2.models.Bin;
import hr.fer.zemris.optjava.dz4.models.solutions.BinContainer;
import hr.fer.zemris.optjava.dz4.models.decoders.BinContainerDecoder;
import hr.fer.zemris.optjava.dz4.models.crossovers.BinCrossoverOperator;
import hr.fer.zemris.optjava.dz4.models.mutations.BinMutationOperator;
import hr.fer.zemris.optjava.dz4.part2.models.Stick;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Entry point for the second part of the homework. Expects one command line
 * argument - path to the file. All other parameters can be set here.
 */
public class BoxFilling {
	public static final double EXPONENT = 1.5;
	public static final double MUTATION_RATE = 0.66;
	public static final int POPULATION_SIZE = 100;
	public static final int MAX_GENERATIONS = 100;
	public static final int TOURNAMENT_SELECTION = 3;
	public static final boolean MINIMIZE = false;

	// replace solution in population even if it has bigger fitness than child
	public static final boolean SWITCH_SOLUTION = true;

	// also default values (if not initialized here)
	static {
		Bin.maxHeight = 20;
		BinContainer.maximumReplacement = 3;
	}

	public static void main(String[] args) throws IOException {
		List<Stick> sticks = loadSticks(args[0]);
		IFunction function = vector -> {
			double total = 0;

			for (double number : vector) {
				total += Math.pow(number, EXPONENT);
			}

			return total / vector.length;
		};

		IDecoder<BinContainer> decoder = new BinContainerDecoder();
		ICrossoverOperator<BinContainer> crossoverOperator = new BinCrossoverOperator();
		IMutationOperator<BinContainer> mutationOperator = new BinMutationOperator(MUTATION_RATE);
		Function<Random, BinContainer> supplier = random -> {
			BinContainer bc = new BinContainer();
			Collections.shuffle(sticks);
			bc.randomize(sticks);
			return bc;
		};
		ISelection<BinContainer> selection = new TournamentSelection<>(TOURNAMENT_SELECTION);

		SteadyStateGA<BinContainer> ga = new SteadyStateGA<>(decoder, crossoverOperator, mutationOperator,
				POPULATION_SIZE, supplier, MAX_GENERATIONS, selection, function, MINIMIZE, SWITCH_SOLUTION);

		BinContainer solution = ga.run();
		System.out.println("----------------------------------------------------------------");
		System.out.println("Final solution:");
		System.out.println("Size: " + solution.size());
		System.out.println(solution.toString());
	}

	private static List<Stick> loadSticks(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path));

		String line = lines.get(0).trim().replaceAll("\\[", "").replaceAll("\\]", "").trim();
		String[] data = line.split(",");

		List<Stick> sticks = new ArrayList<>();
		for (String s : data) {
			int height = Integer.parseInt(s.trim());
			sticks.add(new Stick(height));
		}

		return sticks;
	}
}
