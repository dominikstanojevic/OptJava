package hr.fer.zemris.trisat.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by Dominik on 4.10.2016..
 */
public class Clause {
	private int[] variables;

	public Clause(int[] elements) {
		Objects.requireNonNull(elements, "Elements cannot be null.");

		variables = elements;
	}

	public int getSize() {
		return variables.length;
	}


	public boolean isSatisfied(BitVector assignment) {
		for(int variable : variables) {
			boolean isInverted = variable < 0;
			int index = Math.abs(variable);

			boolean result = isInverted ^ assignment.get(index);
			if(result) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(" + ");

		for(int variable : variables) {
			sj.add(variable < 0 ? "~x" + Math.abs(variable) : "x" + Math.abs(variable));
		}

		return sj.toString();
	}
}
