package hr.fer.zemris.optjava.dz4.part2;

import hr.fer.zemris.optjava.dz4.models.IFunction;
import hr.fer.zemris.optjava.dz4.part2.models.Stick;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Dominik on 4.11.2016..
 */
public class BoxFilling {
    public static final double EXPONENT = 1.5;

    public static void main(String[] args) throws IOException {
        Set<Stick> sticks = loadSticks(args[0]);
        IFunction function = vector -> {
            double total = 0;

            for (double number : vector) {
                total += Math.pow(number, EXPONENT);
            }

            return total / vector.length;
        };
    }

    private static Set<Stick> loadSticks(String path) throws IOException {
        Scanner sc = new Scanner(Paths.get(path));

        Set<Stick> sticks = new HashSet<>();
        while (sc.hasNextInt()) {
            int height = sc.nextInt();
            sticks.add(new Stick(height));
        }

        return sticks;
    }
}
