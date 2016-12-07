package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.neural.FFANN;

import java.io.IOException;

/**
 * Created by Dominik on 7.12.2016..
 */
public class Test {
    public static void main(String[] args) throws IOException {
        String path = "D:\\fer\\5. semestar\\ROPAERUJ\\workspace\\HW07-0036484921\\datoteke\\07-iris-formatirano.data";

        Dataset dataset = Utils.loadData(path);
        FFANN ffann = new FFANN(new int[]{})
    }
}
