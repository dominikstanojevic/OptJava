package hr.fer.zemris.trisat.models;

import java.util.Objects;

/**
 * Created by Dominik on 5.10.2016..
 */
public class MutableBitVector extends BitVector{

	public MutableBitVector(int n) {
		super(n);
	}

	public MutableBitVector(boolean... bits) {
		super(bits);
	}

	public void set(int index, boolean value) {
		int position = getPosition(index);

		vector[position] = value;
	}

	public void increment() {
		boolean carry = true;

		int index = 0;
		do {
			boolean temp = vector[index];
			vector[index] = carry ^ vector[index];
			carry = carry & temp;
			index++;
		} while (carry && index < vector.length);
	}
}
