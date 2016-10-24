package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.algorithms.NumOptAlgorithms;
import hr.fer.zemris.optjava.dz2.models.FunctionAdder;
import hr.fer.zemris.optjava.dz2.models.IHFunction;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Dominik on 15.10.2016..
 */
public class Environment {
    public static void run(
            String[] args, int variables, int rows, int columns,
            Function<double[], IHFunction> function, int candidateSize) {
        if (args.length != 3) {
            System.err.println("Invalid number of command line arguments. Expected 3, given:" +
                               args.length);
            return;
        }

        double[][] data;
        try {
            data = loadData(args[2], rows, columns);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        IHFunction f = createFunctionAdder(data, variables, function);

        int numberOfIteration = Integer.parseInt(args[1]);
        String algorithm = args[0].trim();

        RealVector result;
        switch (algorithm) {
            case "grad":
                result = NumOptAlgorithms
                        .gradientDescentAlgorithm(f, numberOfIteration, candidateSize);
                break;
            case "newton":
                result = NumOptAlgorithms.newtonAlgorithm(f, numberOfIteration, candidateSize);
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm string. Possible " +
                                                   "algorithms are grad and newton. Given: " +
                                                   algorithm);
        }

        double error = Math.sqrt(Math.abs(f.valueAt(result)) / variables);
        System.out.println("Error = " + error);
    }

    private static double[][] loadData(String path, int rows, int columns) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        double[][] values = new double[rows][columns];

        int r = 0;
        for (String l : lines) {
            String line = l.trim();
            if (line.startsWith("#")) {
                continue;
            }

            line = line.replaceAll("\\[", "");
            line = line.replaceAll("\\]", "");
            String[] data = line.split(",");

            double[] row = new double[columns];
            for (int i = 0; i < columns; i++) {
                if (i == columns - 1) {
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

    private static FunctionAdder createFunctionAdder(
            double[][] data, int rows, Function<double[], IHFunction> function) {
        FunctionAdder cf = new FunctionAdder(rows);
        for (double[] row : data) {
            IHFunction f = function.apply(row);
            cf.addFunction(f);
        }

        return cf;
    }
}
