package hr.fer.zemris.trisat.models;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Dominik on 5.10.2016..
 */
public class BitVectorNGenerator implements Iterable<MutableBitVector> {
	private BitVector assignment;

	public BitVectorNGenerator(BitVector assignment) {
		this.assignment = assignment;
	}

	@Override
	public Iterator<MutableBitVector> iterator() {
		return new NeighborhoodIterator();
	}

	public MutableBitVector[] createNeighborhood() {
		int size = assignment.getSize();
		MutableBitVector[] vectors = new MutableBitVector[size];
		Iterator<MutableBitVector> iterator = iterator();

		for (int i = 0; i < size; i++) {
			vectors[i] = iterator.next();
		}

		return vectors;
	}

	private class NeighborhoodIterator implements Iterator<MutableBitVector> {
		private int index;

		@Override
		public boolean hasNext() {
			return index < assignment.getSize();
		}

		@Override
		public MutableBitVector next() {
			if (index >= assignment.getSize()) {
				throw new NoSuchElementException("No more neighbors");
			}

			index++;
			MutableBitVector modified = assignment.copy();
			modified.set(index, !modified.get(index));

			return modified;
		}
	}
}
