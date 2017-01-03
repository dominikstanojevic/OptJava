package hr.fer.zemris.optjava.dz8.models;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Dominik on 18.12.2016..
 */
public class Dataset {
    public RealMatrix input;
    public RealVector output;

    public Dataset(RealMatrix input, RealVector output) {
        this.input = input;
        this.output = output;
    }

    public static Dataset loadData(String path, int variables, int max) throws IOException {
        int rows = max - variables;
        double[][] input = new double[rows][variables];
        double[] output = new double[rows];

        Scanner sc = new Scanner(Paths.get(path));
        List<Double> series = new ArrayList<>();
        while(sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            series.add(Double.parseDouble(line));
        }
        series = normalize(series);

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < variables; j++) {
                input[i][j] = series.get(i + j);
            }

            output[i] = series.get(i + variables);
        }

        return new Dataset(MatrixUtils.createRealMatrix(input), MatrixUtils.createRealVector(output));
    }

    private static List<Double> normalize(List<Double> series) {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        for(int i = 0, n = series.size(); i < n; i++) {
            double number = series.get(i);

            if(number > max) {
                max = number;
            }
            if (number < min) {
                min = number;
            }
        }

        List<Double> normalized = new ArrayList<>();
        double delta = max - min;
        for(int i = 0, n = series.size(); i < n; i++) {
            double number = (series.get(i) - min) / delta;
            normalized.add(2 * number - 1);
        }

        return normalized;
    }
}
