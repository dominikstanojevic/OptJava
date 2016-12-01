package hr.fer.zemris.optjava.dz6.models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dominik on 1.12.2016..
 */
public class CityMap {
    public int numberOfCities;
    public double[][] distances;
    public double[][] heuristicInformation;

    public int neighborhoodSize;
    public int[][] closest;

    public CityMap(int numberOfCities, double[][] distances, double[][] heuristicInformation, int neighborhoodSize) {
        this.numberOfCities = numberOfCities;
        this.distances = distances;
        this.heuristicInformation = heuristicInformation;
        this.neighborhoodSize = neighborhoodSize;

        this.closest = calculateClosest(heuristicInformation);
    }

    public int[] getNeighborhood(int city) {
        return Arrays.copyOfRange(closest[city], 0, neighborhoodSize);
    }

    public static CityMap loadFromFile(String filename, int neighborhoodSize) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);

        int dimension = 0;

        int i = 0;
        String line = lines.get(0);
        while (!line.equals("NODE_COORD_SECTION") && !line.equals("EDGE_WEIGHT_SECTION")) {
            if (line.contains("DIMENSION")) {
                dimension = Integer.parseInt(line.split(":")[1].trim());
            }

            line = lines.get(++i);
        }
        i++;

        if (neighborhoodSize > dimension) {
            throw new RuntimeException();
        }

        Data mapData;
        List<String> data = lines.subList(i, i + dimension);
        if (line.equals("NODE_COORD_SECTION")) {
            mapData = getInformationFromCoordinates(data);
        } else {
            mapData = getInformationFromWeights(data);
        }

        return new CityMap(dimension, mapData.distances, mapData.information, neighborhoodSize);
    }

    private int[][] calculateClosest(double[][] heuristicInformation) {
        int[][] neighborhood = new int[heuristicInformation.length][heuristicInformation.length - 1];

        for (int i = 0; i < heuristicInformation.length; i++) {
            Map<Integer, Double> map = new LinkedHashMap<>();
            for (int j = 0; j < heuristicInformation.length; j++) {
                if (i != j) {
                    map.put(j, heuristicInformation[i][j]);
                }
            }

            neighborhood[i] = map.entrySet().stream().sorted((e1, e2) -> -Double.compare(e1.getValue(), e2.getValue()))
                    .mapToInt(e -> e.getKey()).toArray();
        }

        return neighborhood;
    }

    private static Data getInformationFromWeights(List<String> data) {
        int size = data.size();
        double[][] distances = new double[size][size];
        double[][] information = new double[size][size];

        for (int i = 0; i < information.length; i++) {
            String[] split = data.get(i).split(" ");
            for (int j = i + 1; j < information.length; j++) {
                distances[i][j] = distances[j][i] = Double.parseDouble(split[j]);
                information[i][j] = information[j][i] = 1 / distances[i][j];
            }
        }

        return new Data(distances, information);
    }

    private static Data getInformationFromCoordinates(List<String> data) {
        double[][] coordinates = readCoordinates(data);

        double[][] distances = new double[coordinates.length][coordinates.length];
        double[][] information = new double[coordinates.length][coordinates.length];
        for (int i = 0; i < information.length; i++) {
            for (int j = i + 1; j < information.length; j++) {
                distances[i][j] = distances[j][i] = calculateDistance(coordinates[i], coordinates[j]);
                information[i][j] = information[j][i] = 1 / distances[i][j];
            }
        }

        return new Data(distances, information);
    }

    private static double calculateDistance(double[] first, double[] second) {
        double distanceX = Math.abs(first[0] - second[0]);
        double distanceY = Math.abs(first[1] - second[1]);

        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    private static double[][] readCoordinates(List<String> data) {
        int size = data.size();
        double[][] coordinates = new double[size][2];

        for (int i = 0; i < size; i++) {
            String[] split = data.get(i).split(" ");

            coordinates[i][0] = Double.parseDouble(split[1]);
            coordinates[i][1] = Double.parseDouble(split[2]);
        }

        return coordinates;
    }

    private static class Data {
        double[][] distances;
        double[][] information;

        public Data(double[][] distances, double[][] information) {
            this.distances = distances;
            this.information = information;
        }
    }
}
