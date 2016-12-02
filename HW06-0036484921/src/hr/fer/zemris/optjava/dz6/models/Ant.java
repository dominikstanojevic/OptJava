package hr.fer.zemris.optjava.dz6.models;

import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Dominik on 1.12.2016..
 */
public class Ant {
    public double length;
    public int[] visitedCities;

    @Override
    public String toString() {
        String distance = "Total distance: " + length + "\nVisited cities: ";
        StringJoiner sj = new StringJoiner("->");

        boolean[] visited = new boolean[visitedCities.length];
        for (int city : visitedCities) {
            if(visited[city]) {
                throw new RuntimeException("E jebiga");
            }
            visited[city] = true;

            sj.add(Integer.toString(city + 1));
        }

        return distance + sj.toString();
    }
}
