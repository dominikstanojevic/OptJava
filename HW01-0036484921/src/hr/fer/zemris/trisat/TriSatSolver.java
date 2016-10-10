package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.algorithms.Algorithm1;
import hr.fer.zemris.trisat.algorithms.Algorithm2;
import hr.fer.zemris.trisat.algorithms.Algorithm3;
import hr.fer.zemris.trisat.models.SATFormula;
import hr.fer.zemris.trisat.parser.Parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Dominik on 4.10.2016..
 */
public class TriSatSolver {
	public static final Random RANDOM = new Random();

	private static Map<String, Function<SATFormula, Runnable>> algorithms = new HashMap<>();

	static {
		algorithms.put("1", formula -> new Algorithm1(formula));
		algorithms.put("2", formula -> new Algorithm2(formula));
		algorithms.put("3", formula -> new Algorithm3(formula));
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Invalid number of arguments. Expected 2, got: " + args.length);
			System.exit(-1);
		}

		String path = args[1];
		SATFormula formula = loadProgram(path);

		String algorithm = args[0];
		runAlgorithm(formula, algorithm);
	}

	private static void runAlgorithm(SATFormula formula, String algorithm) {
		Function<SATFormula, Runnable> a = algorithms.get(algorithm);
		if (a == null) {
			System.err.println("Given algorithm does not exist.");
			System.exit(-1);
		} else {
			a.apply(formula).run();
		}
	}

	public static SATFormula loadProgram(String path) {
		try {
			String program =
					new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
			Parser parser = new Parser(program);
			return parser.getFormula();
		} catch (IOException e) {
			System.err.println("File: " + path + " not found.");
			System.exit(-1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		return null;
	}
}
