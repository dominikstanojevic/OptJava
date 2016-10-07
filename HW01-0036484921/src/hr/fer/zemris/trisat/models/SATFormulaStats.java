package hr.fer.zemris.trisat.models;

import java.util.Objects;

/**
 * Created by Dominik on 5.10.2016..
 */
public class SATFormulaStats {
	private SATFormula formula;

	private double[] post;
	private int numberOfSatisfied;
	private boolean isSatisfied;
	private double percentageBonus;

	public static final double PERCENTAGE_CONSTANT_UP = 0.01;
	public static final double PERCENTAGE_CONSTANT_DOWN = 0.1;
	public static final int PERCENTAGE_UNIT_AMOUNT = 50;

	public SATFormulaStats(SATFormula formula) {
		Objects.requireNonNull(formula, "Formula cannot be null.");

		this.formula = formula;
		post = new double[formula.getNumberOfClauses()];
	}

	public void setAssignment(BitVector assignment, boolean updatePercentages) {
		numberOfSatisfied = 0;
		percentageBonus = 0;

		int numberOfClauses = formula.getNumberOfClauses();
		for (int i = 1; i <= numberOfClauses; i++) {
			Clause clause = formula.getClause(i);
			boolean isSatisfied = clause.isSatisfied(assignment);

			if (isSatisfied) {
				numberOfSatisfied++;
			}

			if (updatePercentages) {
				updatePercentages(isSatisfied, i);
			}

			calculateBonus(isSatisfied, i);
		}

		isSatisfied = numberOfSatisfied == numberOfClauses;
	}

	private void calculateBonus(boolean isSatisfied, int i) {
		if(isSatisfied) {
			percentageBonus += PERCENTAGE_UNIT_AMOUNT * (1 - post[i - 1]);
		} else {
			percentageBonus -= PERCENTAGE_UNIT_AMOUNT * (1 - post[i - 1]);
		}
	}

	private void updatePercentages(boolean isSatisfied, int position) {
		if (isSatisfied) {
			post[position - 1] += (1 - post[position - 1]) * PERCENTAGE_CONSTANT_UP;
		} else {
			post[position - 1] += (0 - post[position - 1]) * PERCENTAGE_CONSTANT_DOWN;
		}
	}

	public int getNumberOfSatisfied() {
		return numberOfSatisfied;
	}

	public boolean isSatisfied() {
		return isSatisfied;
	}

	public double getPercentageBonus() {
		return percentageBonus;
	}

	public double getPercentage(int index) {
		int position = getPosition(index);
		return post[position];
	}

	private int getPosition(int index) {
		if (index < 1 || index > post.length) {
			throw new IndexOutOfBoundsException(
					"Index not from valid range. Valid range is " + "from 1 to " +
					post.length + ".");
		}

		return index - 1;
	}

	public SATFormula getFormula() {
		return formula;
	}
}
