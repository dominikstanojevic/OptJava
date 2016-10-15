package hr.fer.zemris.optjava.dz2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

		double[][] columns;
		try {
			columns = loadData(args[2]);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return;
		}
	}

	private static double[][] loadData(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path));
		double[][] values = new double[10][11];

		int r = 0;
		for(String l : lines) {
			String line = l.trim();
			if(line.startsWith("#")) {
				continue;
			}

			line = line.replaceAll("\\[", "");
			line = line.replaceAll("\\]", "");
			String[] data = line.split(",");

			double[] row = new double[11];
			for(int i = 0; i < 11; i++) {
				if (i == 10) {
					row[0] = - Double.parseDouble(data[i]);
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
