package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.models.MutableBitVector;
import hr.fer.zemris.trisat.models.SATFormula;

import java.util.Objects;

/**
 * Created by Dominik on 5.10.2016..
 */
public class Algorithm1 implements Runnable {
	private SATFormula formula;

	public Algorithm1(SATFormula formula) {
		Objects.requireNonNull(formula, "Formula cannot be null.");

		this.formula = formula;
	}

	@Override
	public void run() {
		MutableBitVector vector = new MutableBitVector(formula.getNumberOfVariables());

		long n = (long) Math.pow(2, formula.getNumberOfVariables());
		for(long i = 0; i < n; i++) {
			if(formula.isSatisfied(vector)) {
				System.out.println(vector);
			}

			vector.increment();
		}
	}
}
