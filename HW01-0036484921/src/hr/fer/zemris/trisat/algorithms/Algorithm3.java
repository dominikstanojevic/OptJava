package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.TriSatSolver;
import hr.fer.zemris.trisat.models.BitVector;
import hr.fer.zemris.trisat.models.BitVectorNGenerator;
import hr.fer.zemris.trisat.models.SATFormula;
import hr.fer.zemris.trisat.models.SATFormulaStats;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dominik on 5.10.2016..
 */
public class Algorithm3 implements Runnable {
	private SATFormulaStats stats;

	public static final int NUMBER_OF_BEST = 1;
	private static final int ITERATIONS = 100_000;

	public int it;

	public Algorithm3(SATFormula formula) {
		stats = new SATFormulaStats(formula);
	}

	@Override
	public void run() {
		int n = stats.getFormula().getNumberOfVariables();
		it = 0;

		BitVector vector = new BitVector(TriSatSolver.RANDOM, n);
		Data data = new Data(vector, fitnessFunction(vector), stats.isSatisfied());

		for(int i = 0; i < ITERATIONS; i++) {
			try {
				if(data.isSatisfied) {
					System.out.println("Solution found: " + vector);
					System.out.println("Iteration: " + i);

					it = i;
					return;
				}
			} catch (Exception e) {
				System.out.println("iznimka");
			}

			List<Data> best = getBest(vector);
			int rnd = TriSatSolver.RANDOM.nextInt(NUMBER_OF_BEST);

			data = best.get(rnd);
			vector = data.vector;
			stats.setAssignment(vector, true);
		}

		System.out.println("Maximum iterations reached. Failed.");
		System.out.println("Closest solution: " + vector + ", fitness: " + data.fitness);

		stats.setAssignment(data.vector, true);
		System.out.println(stats.getNumberOfSatisfied());

	}

	private List<Data> getBest(BitVector vector) {
		BitVectorNGenerator generator = new BitVectorNGenerator(vector);
		List<Data> best = new ArrayList<>(NUMBER_OF_BEST);

		for(BitVector v : generator) {
			stats.setAssignment(v, false);

			double fitness = stats.getPercentageBonus();
			boolean isSatisfied = stats.isSatisfied();

			checkBest(best, v, fitness, isSatisfied);
		}

		return best;
	}

	private void checkBest(List<Data> best, BitVector v, double fitness, boolean isSatisfied) {
		if(best.size() < NUMBER_OF_BEST) {
			best.add(new Data(v, fitness, isSatisfied));
			return;
		}

		double min = best.get(0).fitness;
		int index = 0;
		for(int i = 1; i < NUMBER_OF_BEST; i++) {
			if(min >= best.get(i).fitness) {
				min = best.get(i).fitness;
				index = i;
			}
		}

		if(min < fitness) {
			best.set(index, new Data(v, fitness, isSatisfied));
		}
	}

	private double fitnessFunction(BitVector vector) {
		stats.setAssignment(vector, true);
		return stats.getNumberOfSatisfied() + stats.getPercentageBonus();
	}

	public static class Data {
		private BitVector vector;
		private boolean isSatisfied;
		private double fitness;

		public Data(BitVector vector, double fitness, boolean isSatisfied) {
			this.vector = vector;
			this.isSatisfied = isSatisfied;
			this.fitness = fitness;
		}

		public boolean isSatisfied() {
			return isSatisfied;
		}

		public double getFitness() {
			return fitness;
		}
	}
}
