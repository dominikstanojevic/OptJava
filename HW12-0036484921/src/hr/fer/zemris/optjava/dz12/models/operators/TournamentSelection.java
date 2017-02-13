package hr.fer.zemris.optjava.dz12.models.operators;

import hr.fer.zemris.optjava.dz12.models.Ant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dominik on 12.2.2017..
 */
public class TournamentSelection {
    private int size;

    public TournamentSelection(int size) {
        this.size = size;
    }

    public Ant select(List<Ant> population) {
        Random random = ThreadLocalRandom.current();
        int populationSize = population.size();
        Set<Ant> tournament = new HashSet<>();

        while(tournament.size() < size) {
            int index = random.nextInt(populationSize);
            tournament.add(population.get(index));
        }

        List<Ant> listTournament = new ArrayList<>(tournament);
        listTournament.sort(Comparator.reverseOrder());

        return listTournament.get(0);
    }
}
