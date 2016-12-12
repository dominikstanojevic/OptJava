package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.models.neural.ActivationFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 4.12.2016..
 */
public class Utils {
    public static final Random RANDOM = ThreadLocalRandom.current();

    public static RealMatrix createRandomWeights(int rows, int columns, double lowerBound, double upperBound) {
        double[][] matrix = new double[rows + 1][columns];

        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = RANDOM.nextDouble() * (upperBound - lowerBound) + lowerBound;
            }
        }

        return MatrixUtils.createRealMatrix(matrix);
    }

    public static RealVector createRandomVector(int size, double lowerBound, double upperBound) {
        double[] vector = new double[size];
        for (int i = 0; i < size; i++) {
            vector[i] = RANDOM.nextDouble() * (upperBound - lowerBound) + lowerBound;
        }

        return new ArrayRealVector(vector);
    }

    public static RealVector map(RealVector vector, ActivationFunction function) {
        double[] result = new double[vector.getDimension()];

        for (int i = 0, n = vector.getDimension(); i < n; i++) {
            result[i] = function.valueAt(vector.getEntry(i));
        }

        return new ArrayRealVector(result);
    }

    public static RealVector ebeMultiply(RealVector firstVector, RealVector secondVector) {
        if (firstVector.getDimension() != secondVector.getDimension()) {
            throw new DimensionMismatchException(secondVector.getDimension(), firstVector.getDimension());
        }

        double[] result = new double[firstVector.getDimension()];

        for (int i = 0; i < result.length; i++) {
            result[i] = firstVector.getEntry(i) * secondVector.getEntry(i);
        }

        return new ArrayRealVector(result);
    }

    public static Dataset loadData(String path) throws IOException {
        Scanner sc = new Scanner(Paths.get(path));

        List<double[]> inputs = new ArrayList<>();
        List<double[]> outputs = new ArrayList<>();

        while (sc.hasNextLine()) {
            String[] data = sc.nextLine().trim().split(":");

            double[] in = getArrayFromStringArray(data[0].trim().replaceAll("[()]", "").split(","));
            double[] out = getArrayFromStringArray(data[1].trim().replaceAll("[()]", "").split(","));

            inputs.add(in);
            outputs.add(out);
        }

        int n = inputs.size();
        double[][] inputMatrix = new double[n][inputs.get(0).length];
        double[][] outputMatrix = new double[n][outputs.get(0).length];

        for (int i = 0; i < n; i++) {
            inputMatrix[i] = inputs.get(i);
            outputMatrix[i] = outputs.get(i);
        }

        return new Dataset(inputMatrix, outputMatrix);
    }

    private static double[] getArrayFromStringArray(String[] data) {
        double[] numbers = new double[data.length];

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Double.parseDouble(data[i]);
        }

        return numbers;
    }
}
