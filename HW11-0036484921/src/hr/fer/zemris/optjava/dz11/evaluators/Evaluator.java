package hr.fer.zemris.optjava.dz11.evaluators;

import hr.fer.zemris.optjava.dz11.GrayScaleImage;
import hr.fer.zemris.optjava.dz11.solutions.GASolution;

/**
 * Created by Dominik on 20.1.2017..
 */
public class Evaluator implements IGAEvaluator<GASolution<int[]>> {
    private GrayScaleImage template;
    private ThreadLocal<GrayScaleImage> threadLocal;

    public Evaluator(GrayScaleImage template) {
        this.template = template;
        threadLocal = ThreadLocal.withInitial(() -> new GrayScaleImage(template.getWidth(), template.getHeight()));
    }

    public GrayScaleImage draw(GASolution<int[]> solution, GrayScaleImage im) {
        int[] data = solution.getData();
        byte bgcol = (byte) data[0];
        im.clear(bgcol);
        int n = (data.length - 1) / 5;
        int index = 1;
        for (int i = 0; i < n; i++) {
            im.rectangle(data[index], data[index + 1], data[index + 2], data[index + 3], (byte) data[index +4]);
            index += 5;
        }

        return im;
    }

    @Override
    public void evaluate(GASolution<int[]> solution) {
        GrayScaleImage im = threadLocal.get();
        draw(solution, im);

        byte[] data = im.getData();
        byte[] tdata = template.getData();
        int w = im.getWidth();
        int h = im.getHeight();

        double error = 0;
        int index2 = 0;
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                error += Math.abs(((int) data[index2] & 0xFF) - ((int) tdata[index2] & 0xFF));
                index2++;
            }
        }

        solution.fitness = -error;
    }

    public GrayScaleImage getImage() {
        return threadLocal.get();
    }
}
