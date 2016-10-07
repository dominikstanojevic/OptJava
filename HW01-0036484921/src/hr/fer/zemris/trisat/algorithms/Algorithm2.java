package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.TriSatSolver;
import hr.fer.zemris.trisat.models.BitVector;
import hr.fer.zemris.trisat.models.BitVectorNGenerator;
import hr.fer.zemris.trisat.models.SATFormula;
import hr.fer.zemris.trisat.models.SATFormulaStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 5.10.2016..
 */
public class Algorithm2 implements Runnable {
	private SATFormulaStats stats;

	private static final int ITERATIONS = 100_000;

	public Algorithm2(SATFormula formula) {
		stats = new SATFormulaStats(formula);
	}

	@Override
	public void run() {
		int n = stats.getFormula().getNumberOfVariables();
		int c = stats.getFormula().getNumberOfClauses();

		BitVector vector = new BitVector(TriSatSolver.RANDOM, n);
		int fitness = getFitness(vector);


		for (int i = 0; i < ITERATIONS; i++) {
			if (fitness == c) {
				System.out.println("Solution found: " + vector);
				return;
			}

			Pair<Integer, List<BitVector>> best = getBest(vector);

			if (fitness > best.getLeft()) {
				System.out.println("Local optimum found. Failed.");
				System.out.println("Closest solution: " + vector + ", fitness: " + fitness);
				return;
			} else {
				int size = best.getRight().size();
				int index = TriSatSolver.RANDOM.nextInt(size);
				vector = best.getRight().get(index);
				fitness = best.getLeft();
			}
		}

		System.out.println("Maximum iterations reached. Failed.");
		System.out.println("Closest solution: " + vector + ", fitness: " + fitness);
	}

	private int getFitness(BitVector vector) {
		stats.setAssignment(vector, false);
		return stats.getNumberOfSatisfied();
	}

	private Pair<Integer, List<BitVector>> getBest(BitVector vector) {
		BitVectorNGenerator generator = new BitVectorNGenerator(vector);
		List<BitVector> best = new ArrayList<>();

		int maxFit = 0;
		for (BitVector v : generator) {
			int fitness = getFitness(v);

			if(fitness > maxFit) {
				best.clear();
				maxFit = fitness;
			}

			if (fitness == maxFit) {
				best.add(v);
			}
		}

		return new Pair<>(maxFit, best);
	}

	public static class Pair<L, R> {

		private final L left;
		private final R right;

		public Pair(L left, R right) {
			this.left = left;
			this.right = right;
		}

		public L getLeft() {
			return left;
		}

		public R getRight() {
			return right;
		}

		@Override
		public int hashCode() {
			return left.hashCode() ^ right.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Pair)) {
				return false;
			}
			Pair pairo = (Pair) o;
			return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
		}

	}
}
