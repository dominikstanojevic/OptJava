package hr.fer.zemris.trisat.models;

import java.util.Objects;

/**
 * Created by Dominik on 4.10.2016..
 */
public class SATFormula {
	private int numberOfVariables;
	private Clause[] clauses;

	public SATFormula(int numberOfVariables, Clause[] clauses) {
		Objects.requireNonNull(clauses, "Clauses cannot be null");

		this.numberOfVariables = numberOfVariables;
		this.clauses = clauses;
	}

	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	public Clause getClause(int index) {
		int position = getPosition(index);

		return clauses[position];
	}

	private int getPosition(int index) {
		if (index < 1 || index > clauses.length) {
			throw new IndexOutOfBoundsException(
					"Illegal index for clause. Valid range is " + "from 1 to " +
					clauses.length + ".");
		}

		return index - 1;
	}

	public int getNumberOfClauses() {
		return clauses.length;
	}

	public boolean isSatisfied(BitVector assignment) {
		Objects.requireNonNull(assignment, "Assignment cannot be null.");

		for (Clause clause : clauses) {
			if (!clause.isSatisfied(assignment)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Clause clause : clauses) {
			sb.append("(" + clause + ")");
		}

		return sb.toString();
	}
}
