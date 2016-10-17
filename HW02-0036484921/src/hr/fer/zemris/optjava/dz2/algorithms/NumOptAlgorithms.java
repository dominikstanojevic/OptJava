package hr.fer.zemris.optjava.dz2.algorithms;

import hr.fer.zemris.optjava.dz2.models.IFunction;
import hr.fer.zemris.optjava.dz2.models.IHFunction;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * Created by Dominik on 12.10.2016..
 */
public class NumOptAlgorithms {
    private static final Random RANDOM = new Random();
    private static Set<Point2D> points = new HashSet<>();

    public static RealVector gradientDescentAlgorithm(
            IFunction function, int maxIterations, int candidateSize) {
        Function<RealVector, RealVector> f = vector -> getGradientDescent(function, vector);

        return lineSearchAlgorithm(function, maxIterations, candidateSize, f);
    }

    private static RealVector lineSearchAlgorithm(
            IFunction function, int maxIterations, int candidateSize,
            Function<RealVector, RealVector> directionFunction) {
        Objects.requireNonNull(function, "ScalarFunction cannot be null.");

        RealVector candidate = generateCandidate(candidateSize);
        System.out.println("Generated candidate: " + candidate.toString());
        for (int i = 0; i < maxIterations; i++) {
            //addPoint(candidate.getEntry(0), candidate.getEntry(1));

            if (isOptimal(function, candidate)) {
                break;
            }

            RealVector d = directionFunction.apply(candidate);
            double lambda = bisectionSearch(function, candidate, d);
            candidate = candidate.add(d.mapMultiply(lambda));

            System.out
                    .println("Iteration: " + (i + 1) + ", candidate: " + candidate.toString());
        }

        //exportPoints();
        //points.clear();
        return candidate;
    }

    private static void exportPoints() {
        StringJoiner sj = new StringJoiner(",");
        for (Point2D point : points) {
            sj.add("{" + point.getX() + ", " + point.getY() + "}");
        }

        try {
            Files.write(Paths.get("tocke.txt"), sj.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int INCREMENT_MULTIPLIER = 2;
    private static final double THRESHOLD = 1e-2;

    private static double bisectionSearch(
            IFunction function, RealVector vector, RealVector d) {
        double lower = 0, upper = 1;
        while (getDerivative(function, vector, d, upper) <= 0) {
            upper *= INCREMENT_MULTIPLIER;
        }

        double lambda;
        while (true) {
            lambda = (lower + upper) / 2;
            double result = getDerivative(function, vector, d, lambda);

            if (result < THRESHOLD) {
                break;
            } else if (result < 0) {
                lower = lambda;
            } else {
                upper = lambda;
            }
        }

        return lambda;
    }

    private static double getDerivative(
            IFunction function, RealVector vector, RealVector direction, double lambda) {
        RealVector gradient =
                function.gradientValueAt(vector.add(direction.mapMultiply(lambda)));
        RealMatrix transposedGradient = MatrixUtils.createRowRealMatrix(gradient.toArray());
        RealMatrix dAsMatrix = MatrixUtils.createColumnRealMatrix(direction.toArray());

        return transposedGradient.multiply(dAsMatrix).getEntry(0, 0);
    }

    private static RealVector getGradientDescent(IFunction function, RealVector values) {
        return function.gradientValueAt(values).mapMultiply(-1);
    }

    private static boolean isOptimal(IFunction function, RealVector candidate) {
        return Math.abs(function.gradientValueAt(candidate).getNorm()) < 1E-6;
    }

    public static RealVector newtonAlgorithm(
            IHFunction function, int maxIterations, int candidateSize) {
        Function<RealVector, RealVector> f = vector -> getNewtonVector(function, vector);

        return lineSearchAlgorithm(function, maxIterations, candidateSize, f);
    }

    private static RealVector getNewtonVector(IHFunction function, RealVector vector) {
        RealMatrix hessian = function.getHessianMatrixAt(vector);
        RealMatrix hessianInverse = new LUDecomposition(hessian).getSolver().getInverse();
        RealMatrix gradient =
                MatrixUtils.createColumnRealMatrix(function.gradientValueAt(vector).toArray());

        return hessianInverse.scalarMultiply(-1).multiply(gradient).getColumnVector(0);
    }

    private static RealVector generateCandidate(int size) {
        double[] values = new double[size];
        for (int i = 0; i < size; i++) {
            values[i] = RANDOM.nextDouble() * 10 - 5;
        }

        return new ArrayRealVector(values);
    }

    private static void addPoint(double x, double y) {
        Point2D point = new Point2D(x, y);
        points.add(point);
    }
}
