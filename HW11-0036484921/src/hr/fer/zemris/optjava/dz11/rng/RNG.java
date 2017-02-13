package hr.fer.zemris.optjava.dz11.rng;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Dominik on 20.1.2017..
 */
public class RNG {
    private static IRNGProvider rngProvider;

    static {
        Properties properties = new Properties();
        try (InputStream input = RNG.class.getClassLoader().getResourceAsStream("rng-config.properties")) {
            properties.load(input);

            String className = properties.getProperty("rng-provider");
            rngProvider = (IRNGProvider) RNG.class.getClassLoader().loadClass(className).newInstance();
        } catch (IOException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }
}
