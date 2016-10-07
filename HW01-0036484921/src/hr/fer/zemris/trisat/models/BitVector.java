package hr.fer.zemris.trisat.models;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Dominik on 5.10.2016..
 */
public class BitVector {
	protected boolean[] vector;

	public BitVector(Random rand, int numberOfBits) {
		vector = new boolean[numberOfBits];

		for (int i = 0; i < numberOfBits; i++) {
			vector[i] = rand.nextBoolean();
		}
	}

	public BitVector(boolean... bits) {
		Objects.requireNonNull(bits, "Bit vector cannot be null.");
		if (bits.length == 0) {
			throw new IllegalArgumentException("Vector does not contains any element");
		}

		vector = Arrays.copyOf(bits, bits.length);
	}

	public BitVector(int n) {
		vector = new boolean[n];
	}

	public boolean get(int index) {
		int position = getPosition(index);

		return vector[position];
	}

	protected int getPosition(int index) {
		if (index < 1 || index > vector.length) {
			throw new IndexOutOfBoundsException(
					"Invalid index: " + index + ". Valid range " + "is from 1 to " +
					vector.length);
		}
		//offset for -1
		return index - 1;
	}

	public int getSize() {
		return vector.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (boolean bit : vector) {
			sb.append(bit ? 1 : 0);
		}

		return sb.toString();
	}

	public MutableBitVector copy() {
		return new MutableBitVector(vector);
	}
}
