package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.algorithms.NumOptAlgorithms;
import hr.fer.zemris.optjava.dz2.models.CompositeFunction;
import hr.fer.zemris.optjava.dz2.models.IHFunction;
import hr.fer.zemris.optjava.dz2.models.ScalarFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Dominik on 13.10.2016..
 */
public class Sustav {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Invalid number of command line arguments. Expected 3, given:" +
                               args.length);
            return;
        }

        double[][] data;
        try {
            data = loadData(args[2]);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        CompositeFunction cf = new CompositeFunction(10);
        for (double[] row : data) {
            IHFunction function = createFunction(row);
            cf.addFunction(function);
        }

        int numberOfIteration = Integer.parseInt(args[1]);
        String algorithm = args[0].trim();

        RealVector result;
        switch (algorithm) {
            case "grad":
                result = NumOptAlgorithms.gradientDescentAlgorithm(cf, numberOfIteration);
                break;
            case "newton":
                result = NumOptAlgorithms.newtonAlgorithm(cf, numberOfIteration);
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm string. Possible " +
                                                   "algorithms are grad and newton. Given: " +
                                                   algorithm);
        }

        double error = Math.abs(cf.valueAt(result));
        System.out.println("Error = " + error);
    }

    private static IHFunction createFunction(double[] row) {
        Function<RealVector, Double> value = vector -> {
            double result = row[0];
            for (int i = 0, len = vector.getDimension(); i < len; i++) {
                result += row[i + 1] * vector.getEntry(i);
            }
            return result;
        };

        Function<RealVector, Double> function = vector -> Math.pow(value.apply(vector), 2);
        Function<RealVector, RealVector> gradient = vector -> {
            double v = value.apply(vector);

            int size = vector.getDimension();
            RealVector g = new ArrayRealVector(size);
            for (int i = 0; i < size; i++) {
                double entry = 2 * v * row[i + 1];
                g.setEntry(i, entry);
            }

            return g;
        };

        Function<RealVector, RealMatrix> hessian = vector -> {
            int size = vector.getDimension();
            double[][] m = new double[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    m[i][j] = 2 * row[i + 1] * row[j + 1];
                }
            }

            return MatrixUtils.createRealMatrix(m);
        };

        ScalarFunction f = new ScalarFunction(row.length - 1, function, gradient, hessian);
        return f;
    }

    private static double[][] loadData(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        double[][] values = new double[10][11];

        int r = 0;
        for (String l : lines) {
            String line = l.trim();
            if (line.startsWith("#")) {
                continue;
            }

            line = line.replaceAll("\\[", "");
            line = line.replaceAll("\\]", "");
            String[] data = line.split(",");

            double[] row = new double[11];
            for (int i = 0; i < 11; i++) {
                if (i == 10) {
                    row[0] = -Double.parseDouble(data[i]);
                } else {
                    row[i + 1] = Double.parseDouble(data[i]);
                }
            }

            values[r] = row;
            r++;
        }

        return values;
    }
}
