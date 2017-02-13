package hr.fer.zemris.optjava.dz4.models.algorithms;

import hr.fer.zemris.optjava.dz9.models.decoders.IDecoder;
import hr.fer.zemris.optjava.dz9.models.solutions.AbstractSolution;

/**
 * Created by Dominik on 29.12.2016..
 */
public enum Distance {

    DECISION_SPACE {
        @Override
        public <T extends AbstractSolution> double distance(IDecoder<T> decoder, T first, T second) {
            double[] firstArray = decoder.decode(first);
            double[] secondArray = decoder.decode(second);

            return calculateDistance(firstArray, secondArray);
        }
    }, OBJECTIVE_SPACE {
        @Override
        public <T extends AbstractSolution> double distance(IDecoder<T> decoder, T first, T second) {
            return calculateDistance(first.values, second.values);
        }
    };

    private static double calculateDistance(double[] firstArray, double[] secondArray) {
        double total = 0;

        for(int i = 0; i < firstArray.length; i++) {
            double diff = firstArray[i] - secondArray[i];
            total += diff * diff;
        }

        return Math.sqrt(total);
    }

    public abstract <T extends AbstractSolution> double distance(IDecoder<T> decoder, T first, T second);
}
